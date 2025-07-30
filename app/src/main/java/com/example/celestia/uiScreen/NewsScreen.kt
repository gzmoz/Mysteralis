package com.example.celestia.uiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.celestia.ui.theme.Orange26
import com.example.celestia.viewmodel.NewsViewModel

@Composable
fun NewsScreen(navController: NavController) {
    val viewModel: NewsViewModel = viewModel() // buraya ekle
    val articles by viewModel.articles.collectAsState()
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(articles) { index, article ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                        context.startActivity(intent)
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = article.title, style = MaterialTheme.typography.titleMedium, color = Orange26)
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(article.image_url),
                        contentDescription = article.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = article.summary,
                        maxLines = 3,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Scroll sonuna yaklaşınca daha fazla veri çek
            if (index == articles.size - 1) {
                LaunchedEffect(key1 = index) {
                    viewModel.loadMoreArticles()
                }
            }
        }
    }

}