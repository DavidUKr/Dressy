@file:Suppress("DEPRECATION")

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

    interface AuthCallback {
        fun onSuccess(token: String)
        fun onFailure(errorMessage: String)
    }

    private const val PREFS_NAME = "AppPreferences"
    private const val TOKEN_KEY = "jwt_token"

    // Save token to SharedPreferences
    private fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    // Retrieve token from SharedPreferences
    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Login user
    fun loginUser(context: Context, username: String, password: String, callback: AuthCallback) {
        val loginRequest = LoginRequest(username, password)
        ApiClient.authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (!token.isNullOrEmpty()) {
                        saveToken(context, token) // Save the token
                        callback.onSuccess(token) // Notify success
                    } else {
                        callback.onFailure("Login failed: Invalid token received.")
                    }
                } else {
                    callback.onFailure("Login failed: ${response.errorBody()?.string() ?: response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onFailure("Error: ${t.localizedMessage}")
            }
        })
    }

//    fun fetchProtectedData(context: Context, callback: AuthCallback) {
//        val token = getToken(context)
//        if (token != null) {
//            val authService = ApiClient.createAuthService(token)
//            authService.getUserProfile().enqueue(object : Callback<UserProfileResponse> {
//                override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
//                    if (response.isSuccessful) {
//                        val userProfile = response.body()
//                        if (userProfile != null) {
//                            callback.onSuccess("Fetched profile: ${userProfile.username}")
//                        } else {
//                            callback.onFailure("Failed to fetch profile: Empty response.")
//                        }
//                    } else {
//                        callback.onFailure("Failed to fetch profile: ${response.errorBody()?.string() ?: response.message()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
//                    callback.onFailure("Error: ${t.localizedMessage}")
//                }
//            })
//        } else {
//            callback.onFailure("User not authenticated. Please log in.")
//        }
//    }
}

