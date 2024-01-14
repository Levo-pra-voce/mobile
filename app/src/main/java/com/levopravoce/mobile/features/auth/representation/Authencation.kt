package com.levopravoce.mobile.features.auth.representation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.features.auth.domain.AuthRequestStatus
import com.levopravoce.mobile.features.auth.domain.AuthViewModel
import com.levopravoce.mobile.navControllerContext
import com.levopravoce.mobile.routes.Routes
import kotlinx.coroutines.launch

@Composable
fun Authencation(
    content: @Composable () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = navControllerContext.current
    val authUiState = authViewModel.uiState.collectAsState()

    if (authUiState.value.data != null) {
        content()
        return;
    }

    val coroutineScope = rememberCoroutineScope()

    val meRequest = {
        coroutineScope.launch {
            authViewModel.meRequest()
        }
    }

    when (authUiState.value.status) {
        AuthRequestStatus.IDLE -> {
            meRequest()
        }

        AuthRequestStatus.SUCCESS -> {
            content()
        }

        AuthRequestStatus.ERROR -> {
            navController.navigate(
                route = Routes.Auth.ROUTE,
            )
        }

        AuthRequestStatus.LOADING -> {
//            AuthLoading()
        }
    }
}