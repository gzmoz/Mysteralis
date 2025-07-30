package com.example.celestia.uiScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.BadgeDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.celestia.ui.theme.Black40
import com.example.celestia.viewmodel.ApodViewModel
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.celestia.ui.theme.Orange26
import com.example.celestia.ui.theme.lightBlue2
import com.example.celestia.ui.theme.white
import com.example.celestia.ui.theme.white3
import com.example.celestia.ui.theme.yellow2
import java.net.URLDecoder


@RequiresApi(Build.VERSION_CODES.O)
@Composable
/*fun HomeWithBottomAppBar(viewModel: ApodViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController) // alt menü
        },
        containerColor = Black40
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController, viewModel)
        }
    }
}*/
fun HomeWithBottomAppBar(viewModel: ApodViewModel) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = Black40
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "main"  // artık splash değil
            ) {
                composable("main") {
                    MainScreen(viewModel = viewModel, navController = navController)
                }
                composable("favorites") {
                    FavoriteScreen(viewModel = viewModel, navController = navController)
                }
                composable("live") {
                    LiveTrackingScreen(viewModel = viewModel)
                }
                // EPIC, NEWS, etc. de buraya gelebilir

                composable("main/nasa") { NasaScreen(viewModel, navController) }
                composable("main/news") { NewsScreen(navController) }
                composable("chat") { ChatScreen(navController) }
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

            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf("main", "favorites", "live")
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Orange26) {
        items.forEach { route ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (route) {
                            "main" -> Icons.Default.Home
                            "favorites" -> Icons.Default.Favorite
                            "live" -> Icons.Default.Star
                            else -> Icons.Default.Home
                        },
                        contentDescription = route
                    )
                },
                label = {
                    Text(route.replaceFirstChar { it.uppercase() })
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = white,
                    selectedTextColor = white,
                    unselectedIconColor = Black40,
                    unselectedTextColor = Black40,
                    indicatorColor = Orange26 // Seçili item'ın arka planı (vurgusu)
                ),
                //selected = currentRoute == route,
                selected = currentRoute?.startsWith(route) == true,

                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}



