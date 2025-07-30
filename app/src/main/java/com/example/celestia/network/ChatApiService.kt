package com.example.celestia.network

import com.example.celestia.data.ChatRequest
import com.example.celestia.data.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET

//to send data to the Hugging Face Space
interface ChatApiService {
    @POST("search_answer")
    fun askQuestion(@Body request: ChatRequest): Call<ChatResponse>
    //sends the JSON in the ChatRequest model

    @GET("ping")
    fun ping(): Call<String>


}


