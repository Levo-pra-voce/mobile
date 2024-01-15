package com.levopravoce.mobile.features.auth.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.app.representation.Loading
import com.levopravoce.mobile.features.auth.domain.AuthViewModel
import com.levopravoce.mobile.navControllerContext
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch

@Composable
fun Authencation(
    content: @Composable () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val navController = navControllerContext.current
    val authUiState = viewModel.uiState.collectAsState()

    if (authUiState.value.data != null) {
        content()
        return;
    }

    val coroutineScope = rememberCoroutineScope()

    val meRequest = {
        coroutineScope.launch {
            viewModel.meRequest()
        }
    }

    when (authUiState.value.status) {
        RequestStatus.IDLE -> {
            meRequest()
        }

        RequestStatus.SUCCESS -> {
            content()
        }

        RequestStatus.ERROR -> {
            navController.navigate(
                route = Routes.Auth.ROUTE,
            )
        }

        RequestStatus.LOADING -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.customColorsShema.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Loading()
            }
        }
    }
}