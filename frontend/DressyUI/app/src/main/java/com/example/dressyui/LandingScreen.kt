package com.example.dressyui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LandingScreen(onNavigateToLogin: () -> Unit, onNavigateToSignup: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.landing),
            contentDescription = "Rocket Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 62.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Welcome to Dressy!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037),
                    fontSize = 28.sp
                )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Log In Button
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1CEBE))
                ) {
                    Text(text = "LOG IN", fontSize = 20.sp, color = Color.White)
                }

                // Sign Up Button
                Button(
                    onClick = onNavigateToSignup,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC4B7BB))
                ) {
                    Text(text = "SIGN UP", fontSize = 20.sp, color = Color.White)
                }
            }
        }
    }
}