package com.example.celestia.data

import com.example.celestia.model.NewsDataModel

data class NewsResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NewsDataModel>
)
