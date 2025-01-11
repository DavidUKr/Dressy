package com.example.dressyui

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class ImageGenerationRequest(
    val input_image: String,
    val style: String,
    val user_prompt: String,
    val results_count: Int
)

data class ImageGenerationResponse(val generated_image: String)

interface ApiService {
    @POST("/api/v1/generations")
    suspend fun generateImage(
        @Body request: ImageGenerationRequest,
        @Header("Authorization") authToken: String
    ): ImageGenerationResponse
}

// Create Retrofit instance
val retrofit = Retrofit.Builder()
    .baseUrl("https://dressy.onrender.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)