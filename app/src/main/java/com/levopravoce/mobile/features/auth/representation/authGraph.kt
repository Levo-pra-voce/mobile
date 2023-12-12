package com.levopravoce.mobile.features.auth.representation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = "login", route = "auth") {
        composable("login") {
            Text(text = "login")
        }
    }
}

    fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(startDestination = "username", route = "login") {}
}