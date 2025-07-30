package com.example.celestia.uiScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.celestia.viewmodel.ApodViewModel

@Composable
fun NasaScreen(viewModel: ApodViewModel,navController: NavController) {
    LaunchedEffect(Unit) {
        println("Navigated to EpicScreen 🎯")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("This is the EPIC data screen 🪐")
    }
}
