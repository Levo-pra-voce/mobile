package com.levopravoce.mobile.features.forgotPassword.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.error.ErrorUtils
import com.levopravoce.mobile.features.auth.domain.AuthStore
import com.levopravoce.mobile.features.forgotPassword.data.PasswordCodeDTO
import com.levopravoce.mobile.features.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class ForgotPasswordUiState(
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authStore: AuthStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())

    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    suspend fun forgotPasswordRequest(email: String) =
        try {
            this._uiState.value = ForgotPasswordUiState(status = RequestStatus.LOADING)
            val response = userRepository.forgotPassword(email)
            if (response.isSuccessful) {
                this._uiState.value = ForgotPasswordUiState(status = RequestStatus.SUCCESS)
            } else {
                this._uiState.value = ForgotPasswordUiState(
                    status = RequestStatus.ERROR,
                    error = ErrorUtils.parseError(response)
                )
            }
        } catch (e: Exception) {
            this._uiState.value =
                ForgotPasswordUiState(status = RequestStatus.ERROR, error = e.message)
        }

    suspend fun resetPassword(email: String, code: String, password: String) =
        try {
            this._uiState.value = ForgotPasswordUiState(status = RequestStatus.LOADING)
            val passwordCodeDTO = PasswordCodeDTO()
            passwordCodeDTO.email = email
            passwordCodeDTO.code = code
            passwordCodeDTO.password = password
            val response = userRepository.changePassword(passwordCodeDTO)
            if (response.isSuccessful) {
                this._uiState.value = ForgotPasswordUiState(status = RequestStatus.SUCCESS)
            } else {
                this._uiState.value = ForgotPasswordUiState(
                    status = RequestStatus.ERROR,
                    error = ErrorUtils.parseError(response)
                )
            }
        } catch (e: Exception) {
            this._uiState.value =
                ForgotPasswordUiState(status = RequestStatus.ERROR, error = e.message)
        }

    suspend fun checkCode(email: String, code: String) =
        try {
            this._uiState.value = ForgotPasswordUiState(status = RequestStatus.LOADING)
            val response = userRepository.existCode(email, code)
            if (response.isSuccessful) {
                this._uiState.value = ForgotPasswordUiState(status = RequestStatus.SUCCESS)
            } else {
                this._uiState.value = ForgotPasswordUiState(
                    status = RequestStatus.ERROR,
                    error = ErrorUtils.parseError(response)
                )
            }
        } catch (e: Exception) {
            this._uiState.value =
                ForgotPasswordUiState(status = RequestStatus.ERROR, error = e.message)
        }
}