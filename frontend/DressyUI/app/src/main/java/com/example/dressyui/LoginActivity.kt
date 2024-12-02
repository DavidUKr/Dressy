package com.example.dressyui

import LoginRequest
import LoginResponse
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dressyui.ui.theme.DressyUITheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DressyUITheme {
                LoginScreen(
                    onLoginClick = { username, password ->
                        val request = LoginRequest(username, password)

                        ApiClient.authService.login(request).enqueue(object : Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val loginResponse = response.body()
                                    loginResponse?.let {
                                        Toast.makeText(this@LoginActivity, "Login Successful: ${it.message}", Toast.LENGTH_LONG).show()

                                    }
                                } else {
                                    Toast.makeText(this@LoginActivity, "Login Failed: ${response.message()}", Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                            }
                        })
                    },
                    onMainClick = {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
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
    onSignUpClick: () -> Unit,
    onMainClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { onLoginClick(username, password) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD7A685),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
            ) {
                Text(text = "Login", fontSize = 18.sp)
            }

            // Sign Up button
            TextButton(
                onClick = onSignUpClick,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFFD7A685),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 16.dp),            ) {
                Text(
                    text = "Don't have an account? Sign Up",
                    fontSize = 16.sp
                )
            }
            Button(onClick = { onMainClick() }) {
                Text(text = "Main")
            }

        }
    }
}
