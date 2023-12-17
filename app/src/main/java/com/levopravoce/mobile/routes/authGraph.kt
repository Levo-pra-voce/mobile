package com.levopravoce.mobile.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.levopravoce.mobile.features.login.representation.Login

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = "login", route = "auth") {
        composable("login") {
            Login()
        }
    }
}