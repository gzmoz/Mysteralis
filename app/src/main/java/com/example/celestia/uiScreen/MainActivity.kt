package com.example.celestia.uiScreen

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.example.celestia.data.ApodDatabase
import com.example.celestia.network.RetrofitInstance
import com.example.celestia.repository.ApodRepository
import com.example.celestia.ui.theme.CelestiaTheme
import com.example.celestia.viewmodel.ApodViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = ApodDatabase.getInstance(applicationContext)

        val repository = ApodRepository(db.apodDao(), RetrofitInstance.api)

        val viewModel = ApodViewModel(repository)

        setContent {
            CelestiaTheme {
                //HomeWithBottomAppBar(viewModel)
                val navController = rememberNavController()
                NavigationGraph(navController = navController, viewModel = viewModel)

            }
        }
    }
}

