package com.example.celestia.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.celestia.data.ApodEntity
import com.example.celestia.viewmodel.ApodViewModel
import com.example.celestia.ui.theme.Black40

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(viewModel: ApodViewModel, navController: NavController) {
    val apods by viewModel.apods.collectAsState()

    // Sadece favori olanları filtrele
    val favoriteApods = apods.filter { it.isFavorite }

    Scaffold(
        containerColor = Black40,
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Black40)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {
            if (favoriteApods.isEmpty()) {
                Text("No favorites yet.", modifier = Modifier.padding(16.dp), color = Color.White)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 3.dp, vertical = 5.dp)
                ) {
                    items(favoriteApods) { entity ->
                        ApodCard(entity = entity, navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
