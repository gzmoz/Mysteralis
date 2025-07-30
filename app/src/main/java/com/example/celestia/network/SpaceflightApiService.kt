package com.example.celestia.network

import com.example.celestia.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceflightApiService {
    @GET("articles") 
    suspend fun getArticles(
        @Query("news_site") newsSite: String = "NASA",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): NewsResponse
}