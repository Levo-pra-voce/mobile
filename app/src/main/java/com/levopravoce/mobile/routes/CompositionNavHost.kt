package com.levopravoce.mobile.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

val navControllerContext = compositionLocalOf<NavController?> {
    null
}

@Composable
fun CompositionNavHost() {
    val navHost = rememberNavController()
    CompositionLocalProvider(navControllerContext provides navHost) {
        NavHost(
            navController = navHost,
            startDestination = Routes.Home.ROUTE
        ) {
            authGraph()
            homeGraph()
        }
    }
}