package com.cfgdemelo.bigparcialbrasil

import android.annotation.SuppressLint
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
import com.cfgdemelo.bigparcialbrasil.AdminCompetitorsActivity.Companion.COMPETITORS
import com.cfgdemelo.bigparcialbrasil.AdminCompetitorsActivity.Companion.INDEX
import com.cfgdemelo.bigparcialbrasil.AdminCompetitorsActivity.Companion.NAME
import com.cfgdemelo.bigparcialbrasil.AdminCompetitorsActivity.Companion.PHOTO
import com.cfgdemelo.bigparcialbrasil.AdminCompetitorsActivity.Companion.VOTES
import com.cfgdemelo.bigparcialbrasil.AdminCompetitorsActivity.Companion.WALLED
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Admin(LocalContext.current)
                }
            }
        }
    }

    companion object {
        const val COMPETITORS = "competitors"
        const val INDEX = "index"
        const val NAME = "name"
        const val PHOTO = "photo"
        const val VOTES = "votes"
        const val WALLED = "walled"
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun Admin(context: Context) {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReferenceCompetitors = firebaseDatabase.getReference(COMPETITORS)

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
                        index = it[INDEX].toString().toInt(),
                        name = it[NAME].toString(),
                        photo = it[PHOTO].toString(),
                        votes = it[VOTES].toString().toInt(),
                        walled = it[WALLED].toString().toBoolean()
                    )
                )
            }

            competitors.value = competitorsList
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(
                context,
                context.getText(R.string.text_generic_error),
                Toast.LENGTH_SHORT
            ).show()
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
                            databaseReferenceCompetitors.child("${it.index.toString()}/$VOTES")
                                .setValue(0)
                        }
                    ) {
                        Text(text = context.getText(R.string.text_reset_votes).toString())
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    val walled = it.walled
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val child =
                                databaseReferenceCompetitors.child("${it.index.toString()}/$WALLED")
                            if (walled == true) {
                                child.setValue(false)
                            } else {
                                child.setValue(true)
                            }
                        }
                    ) {
                        Text(
                            text = if (walled == true) {
                                context.getText(R.string.text_remove_from_wall).toString()
                            } else {
                                context.getText(R.string.text_put_on_wall).toString()
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