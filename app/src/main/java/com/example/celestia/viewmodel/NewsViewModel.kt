package com.example.celestia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celestia.model.NewsDataModel
import com.example.celestia.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val _articles = MutableStateFlow<List<NewsDataModel>>(emptyList())
    val articles: StateFlow<List<NewsDataModel>> = _articles

    private var currentOffset = 0
    private val pageSize = 10
    private var isLoading = false
    var offset = 0
    val limit = 10

    init {
        loadMoreArticles()
    }

    fun loadMoreArticles() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.newsApi.getArticles(
                    newsSite = "NASA",
                    limit = pageSize,
                    offset = currentOffset
                )
                _articles.value = _articles.value + response.results
                currentOffset += pageSize
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
/*
    private val _articles = MutableStateFlow<List<NewsDataModel>>(emptyList())
    val articles: StateFlow<List<NewsDataModel>> = _articles

    init {
        getArticles()
    }*/

    private fun getArticles() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.newsApi.getArticles()
                _articles.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
