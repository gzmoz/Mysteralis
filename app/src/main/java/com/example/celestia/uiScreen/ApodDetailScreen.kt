package com.example.celestia.uiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.celestia.ui.theme.Orange26
import com.example.celestia.ui.theme.lightBlue
import com.example.celestia.ui.theme.lightBlue2
import com.example.celestia.ui.theme.white2
import com.example.celestia.ui.theme.white4
import java.net.URLEncoder

@Composable
fun ApodDetailScreen(title: String?, explanation: String?, imageUrl: String?, date: String?, navController: NavController ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Geri butonu
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp) // ikon buton alanı
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp) // ikon büyüklüğü
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Başlık ve tarih
                Column(
                    modifier = Modifier.weight(1f) // geri butonu dışındaki tüm alanı kaplasın
                ) {
                    title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Orange26
                        )
                    }

                    date?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineSmall,
                            color = white4
                        )
                    }
                }
            }
        }


        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(8.dp)
                    .clickable {
                        val encodedUrl = URLEncoder.encode(imageUrl ?: "", "UTF-8")
                        navController.navigate("fullscreenImage/$encodedUrl")
                    }
                    .shadow(
                        elevation = 40.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Orange26.copy(alpha = 0.9f),
                        spotColor = Orange26.copy(alpha = 0.9f)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }


        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            if (explanation != null) {
                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 20.dp) // altta buton için boşluk
            ) {

                IconButton(
                    onClick = {
                        navController.navigate("chat")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(50.dp)
                        .background(
                            color = lightBlue,
                            shape = RoundedCornerShape(50)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SmartToy, // AI ikonu
                        contentDescription = "AI Help",
                        tint = Color.White
                    )
                }
            }
        }




    }


}