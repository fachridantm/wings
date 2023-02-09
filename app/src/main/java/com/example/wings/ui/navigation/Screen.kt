package com.example.wings.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object DetailProduct : Screen("home/{productId}") {
        fun createRoute(productId: Long) = "home/$productId"
    }
    object Login : Screen("login")
}