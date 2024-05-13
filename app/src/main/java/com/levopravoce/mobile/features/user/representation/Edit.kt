package com.levopravoce.mobile.features.user.representation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.auth.data.dto.UserType
import kotlinx.coroutines.runBlocking

@Composable
fun UserEdit(
    viewModel: MainViewModel = hiltSharedViewModel()
) {
    val authState = viewModel.authUiStateStateFlow.collectAsStateWithLifecycle()

    when (authState.value.data?.userType) {
        null -> {
            runBlocking {
                viewModel.logout()
            }
        }
        UserType.ENTREGADOR -> DeliveryInfo(
            userDTO = authState.value.data ?: return
        )
        UserType.CLIENTE -> ClientInfo(
            userDTO = authState.value.data ?: return
        )
    }
}