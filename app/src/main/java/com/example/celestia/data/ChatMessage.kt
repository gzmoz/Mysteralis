package com.example.celestia.data

//Represents user and AI messages.
data class ChatMessage(
    val text: String, //holds the content of the message
    val isUser: Boolean //indicating whether the message comes from the user or the AI.
)

//so we can decide whether the messages will ve shown at the right or left in the ChatScreen
