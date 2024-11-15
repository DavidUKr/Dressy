package com.example.dressyui

import LoginRequest
import LoginResponse
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
                        val request = LoginRequest(username, password)

                        ApiClient.authService.login(request).enqueue(object : retrofit2.Callback<LoginResponse> {
                            override fun onResponse(
                                call: retrofit2.Call<LoginResponse>,
                                response: retrofit2.Response<LoginResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val loginResponse = response.body()
                                    loginResponse?.let {
                                        Toast.makeText(this@LoginActivity, "Login Successful: ${it.message}", Toast.LENGTH_LONG).show()
                                        // Navigate to Main Screen or handle token
                                    }
                                } else {
                                    Toast.makeText(this@LoginActivity, "Login Failed: ${response.message()}", Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                            }
                        })
                    },
                    onBackClick = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onSignUpClick = {
                        startActivity(Intent(this, SignUpActivity::class.java)) // Navigate to SignUpActivity
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit // Add this line
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8EDEB))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            color = Color(0xFFFEC5BB),
            fontWeight = FontWeight.Bold,
            fontSize = 31.sp
        )

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

        // Back to Main button
        Button(onClick = onBackClick) {
            Text(text = "Back to Main")
        }

        // Sign Up button
        TextButton(onClick = onSignUpClick) {  // Update this line
            Text(text = "Don't have an account? Sign Up")
        }
    }
}
