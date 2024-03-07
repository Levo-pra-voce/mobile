package com.levopravoce.mobile.common.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.levopravoce.mobile.routes.navControllerContext

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return hiltViewModel<T>(parentEntry)
}

@Composable
inline fun <reified T : ViewModel> hiltSharedViewModel(): T {
    val navController = navControllerContext.current ?: return hiltViewModel<T>()
    val navBackStackEntry = navController.currentBackStackEntry ?: return hiltViewModel<T>()
    return navBackStackEntry.sharedViewModel(navController)
}