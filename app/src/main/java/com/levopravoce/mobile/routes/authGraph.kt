package com.levopravoce.mobile.routes

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.levopravoce.mobile.features.login.representation.Login
import com.levopravoce.mobile.features.register.representation.Register
import com.levopravoce.mobile.features.start.representation.Start

fun NavGraphBuilder.authGraph() {
    navigation(startDestination = Routes.Auth.START, route = Routes.Auth.ROUTE) {
        composable(
            route = Routes.Auth.LOGIN,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
        ) {
            Login()
        }

        composable(
            route = Routes.Auth.START,
        ) {
            Start()
        }

        composable(
            route = Routes.Auth.REGISTER,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
        ) {
            Register()
        }
    }
}