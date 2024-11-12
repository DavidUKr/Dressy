package com.example.dressyui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dressyui.ui.theme.DressyUITheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DressyUITheme {
                LoginScreen(
                    onLoginClick = { username, password ->
                        if (username == "user" && password == "pass") {
                            // Successful login handling
                        } else {
                            // Handle login failure
                        }
                    },
                    onBackClick = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit, onBackClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8EDEB))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally    ) {
        Text(text = "Login", color = Color(0xFFFEC5BB),
            fontWeight = FontWeight.Bold, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Username input field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(onClick = { onLoginClick(username, password) }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onBackClick) {
            Text(text = "Back to Main")
        }
    }
}
