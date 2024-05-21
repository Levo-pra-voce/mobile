package com.levopravoce.mobile.features.app.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.config.PreferencesManager
import com.levopravoce.mobile.features.auth.data.AuthRepository
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.auth.domain.AuthStore
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
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager,
    private val authStore: AuthStore,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())

    val authUiStateStateFlow: StateFlow<AuthUiState> = _uiState

    suspend fun logout() {
        authStore.logout()
        preferencesManager.clear()
        _uiState.value = AuthUiState()
    }

    suspend fun meRequest() {
        _uiState.value = AuthUiState(status = RequestStatus.LOADING)
        try {
            val response = authRepository.me()

            if (response.isSuccessful) {
                val data = response.body()
                if (data?.token != null) {
                    authStore.saveToken(data.token)
                }

                _uiState.value = AuthUiState(data = data, status = RequestStatus.SUCCESS)
            } else {
                authStore.logout()
                _uiState.value = AuthUiState(status = RequestStatus.ERROR, error = response.message())
            }
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

    fun updateUser(updatedUser: UserDTO?) {
        _uiState.value = AuthUiState(data = updatedUser, status = RequestStatus.SUCCESS)
    }
}