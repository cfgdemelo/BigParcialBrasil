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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cfgdemelo.bigparcialbrasil.data.Messages
import com.cfgdemelo.bigparcialbrasil.data.Walled
import com.cfgdemelo.bigparcialbrasil.ui.theme.BigParcialBrasilTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random
import kotlin.collections.HashMap

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BigParcialBrasilTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // on below line we are specifying modifier and color for our app
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // on the below line we are specifying the theme as the scaffold.
                    Scaffold(
                    ) {
                        FirebaseUI(LocalContext.current)
                        // on below line we are calling method to display UI
                    }
                }
            }
        }
    }
}

@Composable
fun FirebaseUI(context: Context) {
    val walled = remember {
        mutableStateOf(ArrayList<Walled>())
    }
    val messages = remember {
        mutableStateOf(Messages())
    }

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReferenceCompetitors = firebaseDatabase.getReference("competitors")
    val databaseReferenceMessages = firebaseDatabase.getReference("messages")

    databaseReferenceCompetitors.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.value as ArrayList<HashMap<String, Any>>
            val walledList = arrayListOf<Walled>()
            value.forEach {
                if (it["walled"] == true) {
                    walledList.add(
                        Walled(
                            index = it["index"].toString().toInt(),
                            name = it["name"].toString(),
                            photo = it["photo"].toString(),
                            votes = it["votes"].toString().toInt()
                        )
                    )
                }
            }

            walled.value = walledList
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, "Fail to get data.", Toast.LENGTH_SHORT).show()
        }
    })

    databaseReferenceMessages.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.value as HashMap<String, String>
            messages.value = Messages(
                title = value["title"],
                subtitle = value["subtitle"],
                footer = value["footer"],
                message = value["message"]
            )
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, "Fail to get data.", Toast.LENGTH_SHORT).show()
        }

    })

    val state = rememberScrollState()

    // on below line creating a column
    // to display our retrieved text.
    Column(
        // adding modifier for our column
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(state),
        // on below line adding vertical and
        // horizontal alignment for column.
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // on below line adding a spacer.
        Spacer(modifier = Modifier.height(20.dp))
        // on below line adding a text
        // to display retrieved message.
        Text(
            // on below line setting text
            // message from message variable.
            text = messages.value.title ?: "",
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            // on below line setting text
            // message from message variable.
            text = messages.value.subtitle ?: "",
            fontSize = 18.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (walled.value.isNotEmpty()) {
            var list = walled.value
            list = getWalledPercentages(list)
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
                    Row(
                        modifier = Modifier
                            .weight(2f)
                            .height(50.dp),
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(getPercentage(it.percentage ?: 0f))
                                .fillMaxHeight(),
                            color = Color(
                                Random.nextInt(256),
                                Random.nextInt(256),
                                Random.nextInt(256)
                            ).copy(alpha = 0.8f)
                        ) {
                            val percentage = it.percentage.toString()
                            Text(
                                text = "${
                                    percentage.substring(
                                        0,
                                        percentage.indexOf(".") + 2
                                    )
                                }%",
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .weight(getPercentage(100f - it.percentage!!))
                                .background(Color.White)
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val votes = it.votes ?: 0
                            databaseReferenceCompetitors.child("${it.index.toString()}/votes")
                                .setValue(votes + 1)
                        }
                    ) {
                        Text(text = it.name.toString())
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
        )
        Text(
            text = messages.value.footer ?: "",
            fontSize = 18.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
        )
        Text(
            text = messages.value.message ?: "",
            fontSize = 18.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

fun getWalledPercentages(list: ArrayList<Walled>): ArrayList<Walled> {
    val returnList = arrayListOf<Walled>()
    var totalVotes = 0

    list.forEach {
        totalVotes += it.votes ?: 0
    }

    list.forEach {
        var percentage = it.votes?.toFloat() ?: 0f
        percentage *= 100
        percentage /= totalVotes
        returnList.add(
            Walled(
                index = it.index,
                name = it.name,
                photo = it.photo,
                votes = it.votes,
                percentage = getPercentage(percentage)
            )
        )
    }

    return returnList
}

private fun getPercentage(percentage: Float): Float {
    return if (percentage == 0f || percentage.isNaN()) {
        0.1f
    } else {
        percentage
    }
}
