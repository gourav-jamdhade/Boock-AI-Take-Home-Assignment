package com.example.boocksaitakehomeassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.boocksaitakehomeassignment.presentation.detail.DetailScreen
import com.example.boocksaitakehomeassignment.presentation.home.HomeScreen
import com.example.boocksaitakehomeassignment.presentation.reader.ReaderScreen

@Composable
fun BoockNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        //Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.Detail.createRoute(bookId))
                }
            )
        }

        //Detail Screen
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType }
            )
        ) {
            DetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onReadClick = { bookId ->
                    navController.navigate(Screen.Reader.createRoute(bookId))
                }
            )
        }

        //Reader Screen
        composable(
            route = Screen.Reader.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType }
            )
        ) {
            ReaderScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}