package com.example.celestia.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.celestia.viewmodel.ApodViewModel
import java.net.URLDecoder

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController, viewModel: ApodViewModel) {
    NavHost(navController,startDestination = "splash"){
        composable("main") { MainScreen(viewModel = viewModel, navController = navController) }
        composable("favorites") { FavoriteScreen(viewModel,navController = navController) }
        composable("live") { LiveTrackingScreen(viewModel) }
        composable("main/nasa") { NasaScreen(viewModel, navController) }
        composable("main/news") { NewsScreen(navController) }
        composable("chat") { ChatScreen(navController) }
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeWithBottomAppBar(viewModel) }

        composable(
            route = "detail/{title}/{explanation}/{imageUrl}/{date}", //declare the path, dynamic data carrying
            arguments = listOf( //means that: this screen will receive 3 parameters and they are string
                navArgument("title"){type = NavType.StringType},
                navArgument("explanation"){type = NavType.StringType},
                navArgument("imageUrl"){type = NavType.StringType},
                navArgument("date"){type = NavType.StringType}
            )
        ){backStackEntry  ->
            val title = URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
            val explanation = URLDecoder.decode(backStackEntry.arguments?.getString("explanation") ?: "", "UTF-8")
            val imageUrl = URLDecoder.decode(backStackEntry.arguments?.getString("imageUrl") ?: "", "UTF-8")
            val date = URLDecoder.decode(backStackEntry.arguments?.getString("date") ?: "", "UTF-8")

            ApodDetailScreen(title = title, explanation = explanation, imageUrl = imageUrl, date = date, navController= navController)

        }

        composable(
            route = "fullscreenImage/{imageUrl}",
            arguments = listOf(navArgument("imageUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUrl = URLDecoder.decode(backStackEntry.arguments?.getString("imageUrl") ?: "", "UTF-8")
            FullscreenImageScreen(imageUrl = imageUrl, navController = navController)

        }

        //composable("calendar"){ CalendarScreen(navController = navController)}

    }
}