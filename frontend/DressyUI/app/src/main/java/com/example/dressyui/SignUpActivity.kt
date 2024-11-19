package com.example.dressyui

import SignupRequest
import SignupResponse
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

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DressyUITheme {
                SignUpScreen(
                    onSignUpClick = { username, password, email ->
                        val request = SignupRequest(username, password, email)

                        ApiClient.authService.signup(request).enqueue(object : retrofit2.Callback<SignupResponse> {
                            override fun onResponse(
                                call: retrofit2.Call<SignupResponse>,
                                response: retrofit2.Response<SignupResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val signupResponse = response.body()
                                    signupResponse?.let {
                                        Toast.makeText(
                                            this@SignUpActivity,
                                            "Signup Successful: ${it.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                                        finish()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Signup Failed: ${response.message()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<SignupResponse>, t: Throwable) {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Error: ${t.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                    },
                    onBackClick = {
                        startActivity(Intent(this, LoginActivity::class.java)) // Navigate back to LoginActivity
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SignUpScreen(onSignUpClick: (String, String,String) -> Unit, onBackClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8EDEB))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
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

        //Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
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

        // Sign Up Button
        Button(onClick = { onSignUpClick(username, password, email) }) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Back to Login button
        TextButton(onClick = onBackClick) {
            Text(text = "Already have an account? Login")
        }
    }
}
