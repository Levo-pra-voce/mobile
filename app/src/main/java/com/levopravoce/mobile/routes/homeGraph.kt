package com.levopravoce.mobile.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.levopravoce.mobile.features.auth.representation.Authentication
import com.levopravoce.mobile.features.chat.representation.Chat
import com.levopravoce.mobile.features.chat.representation.ChatList
import com.levopravoce.mobile.features.configuration.representation.Configuration
import com.levopravoce.mobile.features.home.representation.HomeDecider
import com.levopravoce.mobile.features.themeCustomization.representation.ThemeCustomization
import com.levopravoce.mobile.features.user.representation.UserEdit

fun NavGraphBuilder.homeGraph() {
    navigation(startDestination = Routes.Home.INICIAL, route = Routes.Home.ROUTE) {
        composable(
            route = Routes.Home.INICIAL,
        ) {
            Authentication(content =
            {
                HomeDecider()
            }
            )
        }
        composable(route = Routes.Home.CONFIGURATION) {
            Authentication(content =
            {
                Configuration()
            }
            )
        }
        composable(route = Routes.Home.THEME_CUSTOMIZATION) {
            Authentication(content =
            {
                ThemeCustomization()
            }
            )
        }
        composable(route = Routes.Home.CHAT_LIST) {
            Authentication(content = {
                ChatList()
            })
        }

        composable(route = Routes.Home.USER_EDIT) {
            Authentication(content = {
                 UserEdit()
            })
        }

        composable(route = Routes.Home.MESSAGES, arguments = listOf(navArgument("channelId") {
            type = NavType.LongType
        }, navArgument("channelName") {
            type = NavType.StringType
        })) { backStackEntry ->
            Authentication(content = {
                Chat(
                    channelId = backStackEntry.arguments?.getLong("channelId") ?: 0L,
                    channelName = backStackEntry.arguments?.getString("channelName") ?: ""
                )
            }
            )
        }
    }
}