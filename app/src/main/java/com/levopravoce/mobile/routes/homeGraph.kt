package com.levopravoce.mobile.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.levopravoce.mobile.features.auth.representation.Authencation
import com.levopravoce.mobile.features.chat.representation.Chat
import com.levopravoce.mobile.features.chat.representation.ChatList
import com.levopravoce.mobile.features.configuration.representation.Configuration
import com.levopravoce.mobile.features.home.representation.HomeDecider
import com.levopravoce.mobile.features.themeCustomization.representation.ThemeCustomization

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
        composable(route = Routes.Home.THEME_CUSTOMIZATION) {
            Authencation(content =
            {
                ThemeCustomization()
            }
            )
        }
        composable(route = Routes.Home.CHAT_LIST) {
            Authencation(content = {
                ChatList()
            })
        }
        composable(route = Routes.Home.MESSAGES, arguments = listOf(navArgument("channelId") {
            type = NavType.LongType
        }, navArgument("channelName") {
            type = NavType.StringType
        })) { backStackEntry ->
            Authencation(content = {
                Chat(
                    channelId = backStackEntry.arguments?.getLong("channelId") ?: 0L,
                    channelName = backStackEntry.arguments?.getString("channelName") ?: ""
                )
            }
            )
        }
    }
}