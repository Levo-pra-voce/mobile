package com.levopravoce.mobile.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.levopravoce.mobile.features.auth.representation.Authentication
import com.levopravoce.mobile.features.configuration.representation.Configuration
import com.levopravoce.mobile.features.home.representation.HomeDecider
import com.levopravoce.mobile.features.order.represatation.OrderInfo
import com.levopravoce.mobile.features.relatory.representation.Relatory
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

        composable(route = Routes.Home.USER_EDIT) {
            Authentication(content = {
                 UserEdit()
            })
        }

        composable(route = Routes.Home.SELECT_ORDER) {
            Authentication(content = {
                OrderInfo()
            })
        }

        composable(route = Routes.Home.RELATORY) {
            Authentication(content = {
                Relatory()
            })
        }
    }
}