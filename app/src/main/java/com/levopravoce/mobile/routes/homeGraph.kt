package com.levopravoce.mobile.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.levopravoce.mobile.features.auth.representation.Authencation
import com.levopravoce.mobile.features.configuration.representation.Configuration
import com.levopravoce.mobile.features.home.representation.HomeDecider

fun NavGraphBuilder.homeGraph() {
    navigation(startDestination = Routes.Home.INICIAL, route = Routes.Home.ROUTE) {
        composable(
            route = Routes.Home.INICIAL,
        ) {
            Authencation(content =
                {
                    HomeDecider()
                }
            )
        }
        composable(route = Routes.Home.CONFIGURATION) {
            Authencation(content =
                {
                    Configuration()
                }
            )
        }
    }
}