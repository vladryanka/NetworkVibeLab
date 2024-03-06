package com.smorzhok.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.smorzhok.network.data.Message
import com.smorzhok.network.data.link
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiService {
    @POST("your-endpoint")
    suspend fun sendData(@Body data: List<Message>): Response<Boolean>
}
val retrofit = Retrofit.Builder()
    .baseUrl(link)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)