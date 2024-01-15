package com.levopravoce.mobile.routes

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.levopravoce.mobile.features.login.representation.Login
import com.levopravoce.mobile.features.register.representation.Register
import com.levopravoce.mobile.features.start.representation.Start

const val DURATION_MILLIS = 700

enum class SlideDirection {
    Left, Right
}

fun generateSlideIntoContainerEnterTransition(
    direction: SlideDirection,
    durationMillis: Int
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = (
    {
        val slideDirection = when (direction) {
            SlideDirection.Left -> AnimatedContentTransitionScope.SlideDirection.Left
            SlideDirection.Right -> AnimatedContentTransitionScope.SlideDirection.Right
        }
        slideIntoContainer(
            towards = slideDirection,
            animationSpec = tween(durationMillis)
        )
    }
)

fun generateSlideIntoContainerExitTransition(
    direction: SlideDirection,
    durationMillis: Int
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = (
    {
        val slideDirection = when (direction) {
            SlideDirection.Left -> AnimatedContentTransitionScope.SlideDirection.Left
            SlideDirection.Right -> AnimatedContentTransitionScope.SlideDirection.Right
        }
        slideOutOfContainer(
            towards = slideDirection,
            animationSpec = tween(durationMillis)
        )
    }
)

fun NavGraphBuilder.authGraph() {
    navigation(startDestination = Routes.Auth.START, route = Routes.Auth.ROUTE) {
        composable(
            route = Routes.Auth.LOGIN,
            enterTransition = generateSlideIntoContainerEnterTransition(
                direction = SlideDirection.Left,
                durationMillis = DURATION_MILLIS
            ),
            exitTransition = generateSlideIntoContainerExitTransition(
                direction = SlideDirection.Right,
                durationMillis = DURATION_MILLIS
            ),
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
            enterTransition = generateSlideIntoContainerEnterTransition(
                direction = SlideDirection.Left,
                durationMillis = DURATION_MILLIS
            ),
            exitTransition = generateSlideIntoContainerExitTransition(
                direction = SlideDirection.Left,
                durationMillis = DURATION_MILLIS
            ),
        ) {
            Register()
        }
    }
}