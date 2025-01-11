package com.example.dressyui

import SignupRequest
import SignupResponse
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dressyui.ui.theme.DressyUITheme

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DressyUITheme {
                // Local state for signup
                var isLoading by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf("") }

                SignUpScreen(
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onSignUpClick = { username, email, password ->
                        // Update UI state
                        isLoading = true
                        errorMessage = ""

                        // Prepare API request
                        val request = SignupRequest(username, email, password)

                        ApiClient.authService.signup(request).enqueue(object : retrofit2.Callback<SignupResponse> {
                            override fun onResponse(
                                call: retrofit2.Call<SignupResponse>,
                                response: retrofit2.Response<SignupResponse>
                            ) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    val signupResponse = response.body()
                                    signupResponse?.let {
                                        Toast.makeText(
                                            this@SignUpActivity,
                                            "Signup Successful: ${it.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        // Navigate back to login
                                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                                        finish()
                                    }
                                } else {
                                    // Show error message
                                    errorMessage = "Signup Failed: ${response.message()}"
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<SignupResponse>, t: Throwable) {
                                isLoading = false
                                errorMessage = "Error: ${t.message}"
                            }
                        })
                    },
                    onBackClick = {
                        // Navigate back to login
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}


@Composable
fun SignUpScreen(
    isLoading: Boolean,
    errorMessage: String,
    onSignUpClick: (String, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    // Local states for form inputs
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Validation states
    var isUsernameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.login_image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Sign Up",
                color = Color(0xFFE27239),
                fontWeight = FontWeight.Bold,
                fontSize = 31.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error Message
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Username Input
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    isUsernameValid = it.isNotBlank()
                },
                isError = !isUsernameValid,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
            )
            if (!isUsernameValid) {
                Text(
                    text = "Username is required",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                isError = !isEmailValid,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )
            if (!isEmailValid) {
                Text(
                    text = "Invalid email address",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordValid = it.length >= 6
                },
                isError = !isPasswordValid,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
            if (!isPasswordValid) {
                Text(
                    text = "Password must be at least 6 characters",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button
            Button(
                onClick = {
                    val isFormValid = username.isNotBlank() && isEmailValid && isPasswordValid
                    if (isFormValid) {
                        onSignUpClick(username, email, password)
                    } else {
                        if (username.isBlank()) isUsernameValid = false
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) isEmailValid = false
                        if (password.length < 6) isPasswordValid = false
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD7A685),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(text = "Sign Up", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Back to Login Button
            TextButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 16.dp),
            ) {
                Text(text = "Already have an account? Login", fontSize = 16.sp)
            }
        }
    }
}
