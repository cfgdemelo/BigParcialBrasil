package com.cfgdemelo.bigparcialbrasil

import com.cfgdemelo.bigparcialbrasil.AdminMessagesActivity.Companion.MESSAGES
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
import com.cfgdemelo.bigparcialbrasil.AdminMessagesActivity.Companion.FOOTER
import com.cfgdemelo.bigparcialbrasil.AdminMessagesActivity.Companion.MESSAGE
import com.cfgdemelo.bigparcialbrasil.AdminMessagesActivity.Companion.SUBTITLE
import com.cfgdemelo.bigparcialbrasil.AdminMessagesActivity.Companion.TITLE
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Messages(context = LocalContext.current)
                }
            }
        }
    }

    companion object {
        const val MESSAGES = "messages"
        const val TITLE = "title"
        const val SUBTITLE = "subtitle"
        const val FOOTER = "footer"
        const val MESSAGE = "message"
    }
}

@Composable
fun Messages(context: Context) {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReferenceMessages = firebaseDatabase.getReference(MESSAGES)
    val messages = remember {
        mutableStateOf(com.cfgdemelo.bigparcialbrasil.data.Messages())
    }

    databaseReferenceMessages.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.value as HashMap<String, String>
            messages.value = com.cfgdemelo.bigparcialbrasil.data.Messages(
                title = value[TITLE],
                subtitle = value[SUBTITLE],
                footer = value[FOOTER],
                message = value[MESSAGE]
            )
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
        //Title
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
            var newText by remember { mutableStateOf("") }
            TextField(
                label = { Text(text = context.getText(R.string.text_title).toString()) },
                modifier = Modifier.weight(2f),
                placeholder = { Text(text = text) },
                value = newText,
                onValueChange = {
                    newText = it
                },
                enabled = true,
                readOnly = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    databaseReferenceMessages.child(TITLE).setValue(newText)
                }
            ) {
                Text(text = context.getText(R.string.text_save).toString())
            }
        }

        //Subtitle
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var text by remember { mutableStateOf("") }
            text = messages.value.subtitle ?: ""
            var newText by remember { mutableStateOf("") }
            TextField(
                label = { Text(text = context.getText(R.string.text_subtitle).toString()) },
                modifier = Modifier.weight(2f),
                placeholder = { Text(text = text) },
                value = newText,
                onValueChange = {
                    newText = it
                },
                enabled = true,
                readOnly = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    databaseReferenceMessages.child(SUBTITLE).setValue(newText)
                }
            ) {
                Text(text = context.getText(R.string.text_save).toString())
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        //Footer
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var text by remember { mutableStateOf("") }
            text = messages.value.footer ?: ""
            var newText by remember { mutableStateOf("") }
            TextField(
                label = { Text(text = context.getText(R.string.text_footer).toString()) },
                modifier = Modifier.weight(2f),
                placeholder = { Text(text = text) },
                value = newText,
                onValueChange = {
                    newText = it
                },
                enabled = true,
                readOnly = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    databaseReferenceMessages.child(FOOTER).setValue(newText)
                }
            ) {
                Text(text = context.getText(R.string.text_save).toString())
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        //Message
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var text by remember { mutableStateOf("") }
            text = messages.value.message ?: ""
            var newText by remember { mutableStateOf("") }
            TextField(
                label = { Text(text = context.getText(R.string.text_message).toString()) },
                modifier = Modifier.weight(2f),
                placeholder = { Text(text = text) },
                value = newText,
                onValueChange = {
                    newText = it
                },
                enabled = true,
                readOnly = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    databaseReferenceMessages.child(MESSAGE).setValue(newText)
                }
            ) {
                Text(text = context.getText(R.string.text_save).toString())
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