package com.levopravoce.mobile.features.home.representation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.auth.data.dto.UserType

@Composable
fun HomeDecider(
    mainViewModel: MainViewModel = hiltSharedViewModel()
) {
    val authUiState = mainViewModel.authUiStateStateFlow.collectAsState()

    when (authUiState.value.data?.userType) {
        UserType.CLIENTE -> {
            HomeClient()
        }
        UserType.ENTREGADOR -> {
            HomeDelivery()
        }
        else -> {
            LaunchedEffect(authUiState.value.data?.userType) {
                mainViewModel.logout()
            }
        }
    }
}