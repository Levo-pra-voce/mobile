package com.levopravoce.mobile.features.auth.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.auth.data.AuthRepository
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
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

    private suspend fun <T> requestProcessing(
        vararg parameters: Any, request: suspend (
            Array<out Any>
        ) -> T
    ) {
        _uiState.value = AuthUiState(status = RequestStatus.LOADING)
        try {
            val data = request(parameters);
            _uiState.value = AuthUiState(data = data as UserDTO, status = RequestStatus.SUCCESS)
        } catch (
            e: Exception
        ) {
            _uiState.value = AuthUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun logout() {
        authStore.logout()
        _uiState.value = AuthUiState()
    }

    suspend fun saveToken(token: String) {
        authStore.saveToken(token)
    }

    suspend fun meRequest() {
        requestProcessing {
            authRepository.me()
        }
    }
}