package com.levopravoce.mobile.features.login.representation

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.auth.data.AuthRepository
import com.levopravoce.mobile.features.auth.data.dto.JwtResponseDTO
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.domain.AuthStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class LoginUiState(
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authStore: AuthStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> = _uiState

    suspend fun loginRequest(email: String, password: String): Pair<JwtResponseDTO?, Exception?> {
        this._uiState.value = LoginUiState(status = RequestStatus.LOADING)
        return try {
            val data = authRepository.login(UserDTO(
                email = email,
                password = password
            ))
            authStore.saveToken(data.token)
            this._uiState.value = LoginUiState(status = RequestStatus.SUCCESS)
            Pair(data, null)
        } catch (e: Exception) {
            this._uiState.value = LoginUiState(status = RequestStatus.ERROR, error = e.message)
            Pair(null, e)
        }
    }
}