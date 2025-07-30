package com.example.celestia.data

//When you send a question to the API, you create the body with this structure.
data class ChatRequest(
    val query: String
)

/*in a post request
{
  "query": "what is the milky way?"
}

 */