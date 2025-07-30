package com.example.celestia.network

import com.example.celestia.model.ApodDataModel
import retrofit2.http.GET
import retrofit2.http.Query

//defines how to send request to API
//GET https://api.nasa.gov/planetary/apod
//https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY

//It works with the Retrofit library and can be used to send HTTP GET requests to the API.
interface ApodApiService {
    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key") apiKey: String = "lff6zcQNAncjSn9sYzH1SAKD9fI8SzYIijIuOM9D",
        @Query("date") date: String
    ):ApodDataModel
    //Return type ApodDataModel → The JSON response returned from the API is automatically
    //converted to this model.

}