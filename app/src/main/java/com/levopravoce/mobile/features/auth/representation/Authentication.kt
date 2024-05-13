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
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.app.representation.Loading
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch

@Composable
fun Authentication(
    content: @Composable () -> Unit,
    viewModel: MainViewModel = hiltSharedViewModel()
) {
    val navController = navControllerContext.current
    val authUiState = viewModel.authUiStateStateFlow.collectAsState()

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
            navController?.navigate(
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