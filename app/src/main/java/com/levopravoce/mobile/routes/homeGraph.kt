package com.levopravoce.mobile.routes

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.levopravoce.mobile.features.auth.representation.Authencation

fun NavGraphBuilder.homeGraph() {
    navigation(startDestination = Routes.Home.INICIAL, route = Routes.Home.ROUTE) {
        composable(
            route = Routes.Home.INICIAL,
        ) {
            Authencation(content =
                {
                    Text(text = "Home")
                }
            )
        }
    }
}