package com.example.dressyui

import LoginRequest
import LoginResponse
import SignupRequest
import SignupResponse
import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthManager {

    fun loginUser(context: Context, username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        ApiClient.authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        // Save token to SharedPreferences
                        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("jwt_token", token)
                        editor.apply()

                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to retrieve token.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun fetchProtectedData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        if (token != null) {
            val authService = ApiClient.createAuthService(token)
            // Use authService for making authenticated requests, e.g., fetching user profile, etc.
        } else {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
    fun registerUser(context: Context, username: String, email: String, password: String) {
        val signupRequest = SignupRequest(username, email, password)
        ApiClient.authService.signup(signupRequest).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Registration successful: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
