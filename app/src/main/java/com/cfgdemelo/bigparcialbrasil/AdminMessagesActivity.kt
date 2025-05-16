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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cfgdemelo.bigparcialbrasil.ui.theme.BigParcialBrasilTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminMessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BigParcialBrasilTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Messages(context = LocalContext.current)
                }
            }
        }
    }
}

@Composable
fun Messages(context: Context) {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReferenceMessages = firebaseDatabase.getReference("messages")
    val messages = remember {
        mutableStateOf(com.cfgdemelo.bigparcialbrasil.data.Messages())
    }

    databaseReferenceMessages.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.value as HashMap<String, String>
            messages.value = com.cfgdemelo.bigparcialbrasil.data.Messages(
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
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var text by remember { mutableStateOf("") }
            text = messages.value.title ?: ""
            TextField(
                label = { Text(text = "Título") },
                modifier = Modifier.weight(2f),
                value = text,
                onValueChange = { text = it })
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    databaseReferenceMessages.child("title").setValue(text)
                }
            ) {
                Text(text = "Salvar")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    BigParcialBrasilTheme {
        Messages(context = LocalContext.current)
    }
}