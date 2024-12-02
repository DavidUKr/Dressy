package com.example.dressyui

import AuthService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://dressy.onrender.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
    // Function to provide OkHttpClient with interceptor
    private fun getOkHttpClient(token: String?): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val request: Request = chain.request().newBuilder().apply {
                    if (token != null) {
                        addHeader("Authorization", "Bearer $token")
                    }
                }.build()
                chain.proceed(request)
            }
            .build()
    }

    fun createAuthService(token: String? = null): AuthService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AuthService::class.java)
    }
//    private const val BASE_URL = "https://dressy.onrender.com/"
//
//    val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val authService: AuthService by lazy {
//        retrofit.create(AuthService::class.java)
//    }
}
