package com.example.celestia.data

//the structure of the answer that comes from the AI
data class ChatResponse(
    val answer: String,
    val sources: List<String>
)
