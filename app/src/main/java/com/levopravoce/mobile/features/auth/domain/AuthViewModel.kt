package com.levopravoce.mobile.features.auth.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.auth.data.AuthRepository
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class AuthUiState(
    val data: UserDTO? = null,
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authStore: AuthStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())

    val uiState: StateFlow<AuthUiState> = _uiState

    suspend fun logout() {
        authStore.logout()
        _uiState.value = AuthUiState()
    }

    suspend fun saveToken(token: String) {
        authStore.saveToken(token)
    }

    suspend fun meRequest() {
        _uiState.value = AuthUiState(status = RequestStatus.LOADING)
        try {
            val data = authRepository.me()

            if (data.token != null) {
                authStore.saveToken(data.token)
            }

            _uiState.value = AuthUiState(data = data, status = RequestStatus.SUCCESS)
        } catch (
            e: Exception
        ) {
            _uiState.value = AuthUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }

    fun isDeliveryMan(): Boolean {
        return uiState.value.data?.userType == UserType.ENTREGADOR
    }
}