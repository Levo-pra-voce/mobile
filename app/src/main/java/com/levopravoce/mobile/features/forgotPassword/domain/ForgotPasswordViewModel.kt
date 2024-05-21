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
    var status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())

    private var _email: String? = null
    private var _code: String? = null

    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    suspend fun sendEmail(email: String) = try {
        this._uiState.value = ForgotPasswordUiState(status = RequestStatus.LOADING)
        val response = userRepository.forgotPassword(email)
        if (response.isSuccessful) {
            this._uiState.value =
                ForgotPasswordUiState(status = RequestStatus.SUCCESS)
            this._email = email
        } else {
            this._uiState.value = ForgotPasswordUiState(
                status = RequestStatus.ERROR, error = ErrorUtils.parseError(response)
            )
        }
    } catch (e: Exception) {
        this._uiState.value = ForgotPasswordUiState(status = RequestStatus.ERROR, error = e.message)
    }

    suspend fun resetPassword(newPassword: String, repeatPassword: String) {
        if (newPassword != repeatPassword) {
            this._uiState.value = ForgotPasswordUiState(
                status = RequestStatus.ERROR, error = "As senhas não coincidem"
            )
            return
        }

        try {
            this._uiState.value = ForgotPasswordUiState(status = RequestStatus.LOADING)

            val code = this._code
            if (code == null) {
                this._uiState.value = ForgotPasswordUiState(
                    status = RequestStatus.ERROR, error = "Código não encontrado"
                )
                return
            }

            val email = this._email
            if (email == null) {
                this._uiState.value = ForgotPasswordUiState(
                    status = RequestStatus.ERROR, error = "Email não encontrado"
                )
                return
            }

            val passwordCodeDTO = PasswordCodeDTO()
            passwordCodeDTO.email = email
            passwordCodeDTO.code = code
            passwordCodeDTO.password = newPassword
            val response = userRepository.changePassword(passwordCodeDTO)
            if (response.isSuccessful) {
                this._uiState.value = ForgotPasswordUiState(status = RequestStatus.SUCCESS)
            } else {
                this._uiState.value = ForgotPasswordUiState(
                    status = RequestStatus.ERROR, error = ErrorUtils.parseError(response)
                )
            }
        } catch (e: Exception) {
            this._uiState.value =
                ForgotPasswordUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun checkCode(code: String) {
        try {
            this._uiState.value = ForgotPasswordUiState(status = RequestStatus.LOADING)
            val email = this._email
            if (email == null) {
                this._uiState.value = ForgotPasswordUiState(
                    status = RequestStatus.ERROR, error = "Email não encontrado"
                )
                return
            }

            val body = PasswordCodeDTO()
            body.email = email
            body.code = code

            val response = userRepository.existCode(body)
            if (response.isSuccessful) {
                this._uiState.value = ForgotPasswordUiState(status = RequestStatus.SUCCESS)
                this._code = code
            } else {
                this._uiState.value = ForgotPasswordUiState(
                    status = RequestStatus.ERROR, error = ErrorUtils.parseError(response)
                )
            }
        } catch (e: Exception) {
            this._uiState.value =
                ForgotPasswordUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }

    fun resetRequestStatus() {
        this._uiState.value.status = RequestStatus.IDLE
    }
}