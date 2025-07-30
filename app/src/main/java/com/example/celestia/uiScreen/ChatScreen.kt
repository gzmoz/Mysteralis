package com.example.celestia.uiScreen

import android.content.Intent
import android.net.Uri
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.celestia.data.ChatMessage
import com.example.celestia.data.ChatRequest
import com.example.celestia.network.RetrofitInstance
import com.example.celestia.ui.theme.Orange26
import com.example.celestia.ui.theme.lightBlue
import com.example.celestia.ui.theme.white
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }   //hold the chat history

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        //top bar -> back button+title
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ){
            IconButton(onClick = {navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = white
                )
            }

            Text(text = "AI Chatbot",
                style = MaterialTheme.typography.titleLarge,
                color = white
            )
        }

        //chat message list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            reverseLayout = true // the most recent message will appear at the bottom
        ) {
            items(messages.reversed()) { message->  //why reversed? :Messages flow from top to bottom
                ChatBubble(message)
            }
        }

        //input area and send button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 300.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Orange26.copy(alpha = 0.9f),
                        spotColor = Orange26.copy(alpha = 0.9f)
                    )
                    .background(Color.White, MaterialTheme.shapes.medium)
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .fillMaxWidth(), // kutunun içine sığsın
                    placeholder = { Text("Ask something...") },
                    /*colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent // arka plan rengi görünmesin, Box belirleyecek
                    )*/
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {

                val userMessage = inputText.text.trim()
                if (userMessage.isNotEmpty()) {
                    messages = messages + ChatMessage(userMessage, isUser = true)
                    inputText = TextFieldValue("")

                   /* Dispatchers.IO -> When reading network/file data
                   *  Dispatchers.Main -> When updating UI
                   *  Dispatchers.Default -> CPU intensive operations
                   * */
                    //withContext: allows you to temporarily switch to another dispatcher (thread pool) within your coroutine.
                    // API çağrısı
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = RetrofitInstance.chatApi.askQuestion(ChatRequest(userMessage)).execute()
                            //.execute(): synchronous call → returns the result directly. (alternative: .enqueue() with async callback)
                            if (response.isSuccessful && response.body() != null) {
                                val result = response.body()!! //Converts the response to a model class (ChatResponse).
                                val botMessage = result.answer + "\n\nSources:\n" + result.sources.joinToString("\n") //Converts the answer into text.
                                withContext(Dispatchers.Main) { //Switch to Main Thread to Update UI
                                    messages = messages + ChatMessage(botMessage, isUser = false)
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    messages = messages + ChatMessage("Failed to get answer.", isUser = false)
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                messages = messages + ChatMessage("Error: ${e.message}", isUser = false)
                            }
                        }
                    }
                }
            }, colors = IconButtonDefaults.iconButtonColors(lightBlue)){
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Message",
                    tint = Color.White
                )
            }
        }

    }

}


@Composable
fun ChatBubble(message: ChatMessage) {
    val context = LocalContext.current  //It is a Compose-specific method that we use to access the classic Context object in Jetpack Compose.
    //why do we use LocalContext?:Jetpack Compose no longer uses classic structures like Activities, Fragments, Views, etc.
    // So you can't use things like this or getContext() directly.

    //Context is a system gateway that knows where the Android application is running, what resources it has, and what it can access.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (message.isUser) lightBlue else Color.LightGray,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp)
        ) {
            if (message.isUser) {
                Text(text = message.text)
            } else {
                val annotatedString = buildAnnotatedString {
                    val regex = Regex("""https?://[^\s]+""") //Finds URLs starting with http or https using Regex.
                    val parts = regex.split(message.text) //parts: splits the text by links
                    val links = regex.findAll(message.text).map { it.value }.toList() //links: returns all the links in the text in order

                    var currentIndex = 0
                    for ((i, part) in parts.withIndex()) {
                        append(part)
                        currentIndex += part.length

                        if (i < links.size) {
                            val url = links[i]
                            pushStringAnnotation(tag = "URL", annotation = url) //pushStringAnnotation(...): makes the link clickable
                            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                                append(url)
                            }
                            pop()
                            currentIndex += url.length
                        }
                    }
                }

                ClickableText(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset) //getStringAnnotations(...) Finds the URL at the clicked location
                            .firstOrNull()?.let { annotation ->
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item)) //Commands Android to open the clicked link in the browser
                                context.startActivity(browserIntent)
                            }
                    }
                )
            }
        }
    }
}

