package com.example.dressyui

import LoginRequest
import LoginResponse
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lint.kotlin.metadata.Visibility
import com.example.dressyui.ui.theme.DressyUITheme


class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DressyUITheme {
                var isLoading by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf("") }

                LoginScreen(
                    onLoginClick = { username, password ->
                        if (username.isBlank() || password.isBlank()) {
                            errorMessage = "Username and password cannot be empty"
                            return@LoginScreen
                        }

                        isLoading = true
                        errorMessage = ""
                        AuthManager.loginUser(this, username, password, object : AuthManager.AuthCallback {
                            override fun onSuccess(token: String) {
                                isLoading = false
                                errorMessage = ""

                                Log.d("LoginActivity", "Login successful, navigating to HomeActivity")

                                val intent = Intent(this@LoginActivity, Home::class.java)
                                startActivity(intent)
                                finish()
                            }

                            override fun onFailure(errorMessage: String) {
                                isLoading = false
                                Log.e("LoginActivity", "Login error: $errorMessage")
                            }
                        })
                    },
                    onSignUpClick = {
                        startActivity(Intent(this, SignUpActivity::class.java))
                    },
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }
        }
    }

    companion object {
        const val PREFS_NAME = "AppPreferences"
        const val PREF_TOKEN_KEY = "jwt_token"
    }
}

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onSignUpClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // Password visibility toggle

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                color = Color(0xFFE27239),
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { onLoginClick(username, password) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD7A685),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(text = "Login", fontSize = 18.sp)
                }
            }

            TextButton(
                onClick = onSignUpClick,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFFD7A685),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Don't have an account? Sign Up",
                    fontSize = 16.sp
                )
            }
        }
    }
}
