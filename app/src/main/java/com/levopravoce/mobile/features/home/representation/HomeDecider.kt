package com.levopravoce.mobile.features.home.representation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.auth.domain.AuthViewModel

@Composable
fun HomeDecider(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authUiState = authViewModel.uiState.collectAsState()

    when (authUiState.value.data?.userType) {
        UserType.CLIENTE -> {
            HomeClient()
        }
        UserType.ENTREGADOR -> {
            HomeDelivery()
        }
        else -> {
            LaunchedEffect(authUiState.value.data?.userType) {
                authViewModel.logout()
            }
        }
    }
}