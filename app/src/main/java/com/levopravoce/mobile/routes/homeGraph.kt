package com.levopravoce.mobile.routes

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.levopravoce.mobile.features.auth.representation.Authentication
import com.levopravoce.mobile.features.configuration.representation.Configuration
import com.levopravoce.mobile.features.deliveryList.representation.DeliveryDetails
import com.levopravoce.mobile.features.deliveryList.representation.DeliveryList
import com.levopravoce.mobile.features.home.representation.HomeDecider
import com.levopravoce.mobile.features.order.represatation.OrderInfo
import com.levopravoce.mobile.features.payment.representation.ClientPayment
import com.levopravoce.mobile.features.payment.representation.DeliveryPayment
import com.levopravoce.mobile.features.relatory.representation.Relatory
import com.levopravoce.mobile.features.themeCustomization.representation.ThemeCustomization
import com.levopravoce.mobile.features.tracking.representation.ClientTracking
import com.levopravoce.mobile.features.tracking.representation.DeliveryTracking
import com.levopravoce.mobile.features.user.representation.ChangePassword
import com.levopravoce.mobile.features.user.representation.UserEdit

fun NavGraphBuilder.authComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
        enterTransition,
    popExitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
        exitTransition,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    this.composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
    ) {
        Authentication {
            content(it)
        }
    }
}

fun NavGraphBuilder.homeGraph() {
    navigation(startDestination = Routes.Home.INICIAL, route = Routes.Home.ROUTE) {
        authComposable(
            route = Routes.Home.INICIAL,
        ) {
            HomeDecider()
        }
        authComposable(route = Routes.Home.CONFIGURATION) {
            Configuration()
        }
        authComposable(route = Routes.Home.THEME_CUSTOMIZATION) {
            ThemeCustomization()
        }

        authComposable(route = Routes.Home.USER_EDIT) {
            UserEdit()
        }

        authComposable(route = Routes.Home.SELECT_ORDER) {
            OrderInfo()
        }

        authComposable(route = Routes.Home.RELATORY) {
            Relatory()
        }

        authComposable(route = Routes.Home.CHANGE_PASSWORD) {
            ChangePassword()
        }

        authComposable(route = Routes.Home.DELIVERY_LIST) {
            DeliveryList()
        }

        authComposable(route = Routes.Home.DELIVERY_DETAILS) {
            val deliveryId = it.arguments?.getString("deliveryId") ?: ""
            DeliveryDetails(deliveryId)
        }

        authComposable(route = Routes.Home.DELIVERY_DETAILS) {
            val deliveryId = it.arguments?.getString("deliveryId") ?: ""
            DeliveryDetails(deliveryId)
        }

        authComposable(route = Routes.Home.DELIVERY_TRACKING_CLIENT) {
            ClientTracking()
        }

        authComposable(route = Routes.Home.DELIVERY_TRACKING_DRIVER) {
            DeliveryTracking()
        }

//        authComposable(route = Routes.Home.CLIENT_PAYMENT) {
//            ClientPayment()
//        }
//
//        authComposable(route = Routes.Home.DELIVERY_PAYMENT) {
//            DeliveryPayment()
//        }
    }
}