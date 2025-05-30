package com.cfgdemelo.bigparcialbrasil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.cfgdemelo.bigparcialbrasil.ui.theme.BigParcialBrasilTheme

class AdminEntry : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BigParcialBrasilTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdminOptions(LocalContext.current)
                }
            }
        }
    }
}

@Composable
fun AdminOptions(context: Context) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                context.startActivity(
                    Intent(
                        context,
                        AdminCompetitorsActivity::class.java
                    )
                )
            }) {
                Text(text = context.getText(R.string.text_participants).toString())
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                context.startActivity(
                    Intent(
                        context,
                        AdminMessagesActivity::class.java
                    )
                )
            }) {
                Text(text = context.getText(R.string.text_messages).toString())
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                context.startActivity(
                    Intent(
                        context,
                        MainActivity::class.java
                    )
                )
            }) {
                Text(text = context.getText(R.string.text_user_view).toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BigParcialBrasilTheme {
        AdminOptions(LocalContext.current)
    }
}