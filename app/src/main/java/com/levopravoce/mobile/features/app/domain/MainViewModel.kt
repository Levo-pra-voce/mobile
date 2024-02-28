package com.levopravoce.mobile.features.app.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.config.PreferencesManager
import com.levopravoce.mobile.config.WebSocketClient
import com.levopravoce.mobile.features.auth.data.AuthRepository
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.auth.domain.AuthStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AuthUiState(
    val data: UserDTO? = null,
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager,
    private val authStore: AuthStore,
    private val webSocketClient: WebSocketClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())

    val authUiStateStateFlow: StateFlow<AuthUiState> = _uiState

    override fun onCleared() {
        super.onCleared()
        webSocketClient.disconnect()
    }

    suspend fun logout() {
        authStore.logout()
        webSocketClient.disconnect()
        preferencesManager.clear()
        _uiState.value = AuthUiState()
    }

    suspend fun meRequest() {
        _uiState.value = AuthUiState(status = RequestStatus.LOADING)
        try {
            val data = authRepository.me()

            if (data.token != null) {
                authStore.saveToken(data.token)
                webSocketClient.connect()
            }

            _uiState.value = AuthUiState(data = data, status = RequestStatus.SUCCESS)
        } catch (
            e: Exception
        ) {
            _uiState.value = AuthUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }

    fun isDeliveryMan(): Boolean {
        return authUiStateStateFlow.value.data?.userType == UserType.ENTREGADOR
    }

    fun getFirstName(): String? {
        val name = authUiStateStateFlow.value.data?.name ?: return null
        val names = name.split(" ")

        return names[0]
    }
}