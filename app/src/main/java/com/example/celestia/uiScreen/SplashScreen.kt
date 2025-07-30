package com.example.celestia.uiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.celestia.R
import com.example.celestia.network.RetrofitInstance
import com.example.celestia.ui.theme.Black40
import com.example.celestia.ui.theme.lightBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun  SplashScreen(navController: NavController) {
    /*LaunchedEffect(Unit) ifadesi, Jetpack Compose içinde kullanılan bir yan etki başlatıcı (side-effect launcher)’dır.
     İşlevi, belirli bir koşul gerçekleştiğinde bir kereye mahsus bir işin yapılmasını sağlamaktır.
    */
    val fullText = "Mysteralis"
    var textToShow by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        for (i in 1..fullText.length) {
            textToShow = fullText.substring(0, i)
            delay(150L) // Harf harf yazdırmak için gecikme
        }
        delay(2000L)
        navController.navigate("home"){
            popUpTo("splash"){inclusive = true} //gezinme sırasında önceki ekranları geri tuşuna basınca tekrar görünmeyecek şekilde yığından (stack’ten) çıkar
        }
    }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.chatApi.ping().execute()
                if (response.isSuccessful) {
                    println("Ping successful: ${response.body()}")
                }
            } catch (e: Exception) {
                println("Ping failed: ${e.message}")
            }
        }
    }


    // Arayüz
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logoyu ortada göster
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            // Altına yazıyı harf harf yaz
            Text(
                text = textToShow,
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black40
                )
            )
        }
    }
}