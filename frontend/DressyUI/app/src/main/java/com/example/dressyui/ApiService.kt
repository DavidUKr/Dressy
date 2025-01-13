//package com.example.dressyui
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import retrofit2.Call
//import retrofit2.http.Body
//import retrofit2.http.Multipart
//import retrofit2.http.POST
//import retrofit2.http.Part
//
//data class ImageGenerationRequest(
//    val input_image: String,
//    val style: String,
//    val user_prompt: String,
//    val results_count: Int
//)
//
//interface ApiService {
//    @Multipart
//    @POST("generate-outfit")
//    suspend fun generateImage(
//        @Part("style") style: RequestBody,
//        @Part("user_prompt") userPrompt: RequestBody,
//        @Part("results_count") resultsCount: RequestBody,
//        @Part imagePart: MultipartBody.Part
//    ): GeneratedResponse
//}
