package com.cfgdemelo.bigparcialbrasil

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cfgdemelo.bigparcialbrasil.data.Messages
import com.cfgdemelo.bigparcialbrasil.data.Walled
import com.cfgdemelo.bigparcialbrasil.ui.theme.BigParcialBrasilTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminCompetitorsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BigParcialBrasilTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Admin(LocalContext.current)
                }
            }
        }
    }
}

@Composable
fun Admin(context: Context) {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReferenceCompetitors = firebaseDatabase.getReference("competitors")

    val competitors = remember {
        mutableStateOf(ArrayList<Walled>())
    }

    databaseReferenceCompetitors.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.value as ArrayList<HashMap<String, Any>>
            val competitorsList = arrayListOf<Walled>()
            value.forEach {
                competitorsList.add(
                    Walled(
                        index = it["index"].toString().toInt(),
                        name = it["name"].toString(),
                        photo = it["photo"].toString(),
                        votes = it["votes"].toString().toInt(),
                        walled = it["walled"].toString().toBoolean()
                    )
                )
            }

            competitors.value = competitorsList
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, "Fail to get data.", Toast.LENGTH_SHORT).show()
        }
    })

    val state = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(state),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        val list = competitors.value
        if (list.isNotEmpty()) {
            list.forEach {
                Row(
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(
                            LocalContext.current
                        )
                            .data(it.photo)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .width(80.dp)
                            .height(80.dp)
                            .weight(1f),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            databaseReferenceCompetitors.child("${it.index.toString()}/votes")
                                .setValue(0)
                        }
                    ) {
                        Text(text = "Zerar Votos")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    val walled = it.walled
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val child =
                                databaseReferenceCompetitors.child("${it.index.toString()}/walled")
                            if (walled == true) {
                                child.setValue(false)
                            } else {
                                child.setValue(true)
                            }
                        }
                    ) {
                        Text(
                            text = if (walled == true) {
                                "Desemparedar"
                            } else {
                                "Emparedar"
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AdminPreview() {
    BigParcialBrasilTheme {
        Admin(LocalContext.current)
    }
}