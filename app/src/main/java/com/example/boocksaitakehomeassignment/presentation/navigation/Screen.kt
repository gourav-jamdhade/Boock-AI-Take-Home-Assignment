package com.example.boocksaitakehomeassignment.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{bookId}") {
        fun createRoute(bookId: String) = "detail/$bookId"
    }

    object Reader : Screen("reader/{bookId}") {
        fun createRoute(bookId: String) = "reader/$bookId"

    }
}