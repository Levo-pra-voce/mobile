package com.levopravoce.mobile.features.user.domain

import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.error.APIError
import com.levopravoce.mobile.common.error.ErrorUtils
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.auth.domain.AuthStore
import com.levopravoce.mobile.features.forgotPassword.data.PasswordCodeDTO
import com.levopravoce.mobile.features.forgotPassword.domain.ForgotPasswordUiState
import com.levopravoce.mobile.features.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class UserUiState(
    val data: UserDTO? = null,
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())

    val uiState: StateFlow<UserUiState> = _uiState

    init{
        _uiState.value = UserUiState(
            data = UserDTO(
                userType = UserType.ENTREGADOR
            )
        )
    }

    suspend fun register(userType: UserType, user: UserDTO) {
        _uiState.value = UserUiState(status = RequestStatus.LOADING)
        try {
            val data = userRepository.register(userType.name, user)
            if (data.isSuccessful) {
                _uiState.value = UserUiState(status = RequestStatus.SUCCESS)
            } else {
                _uiState.value = UserUiState(status = RequestStatus.ERROR, error = ErrorUtils.parseError(response = data))
            }

        } catch (
            e: Exception
        ) {
            _uiState.value = UserUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun update(userDTO: UserDTO): Boolean {
        _uiState.value = UserUiState(status = RequestStatus.LOADING)
        try {
            val data = userRepository.update(userDTO)

            if (data.isSuccessful) {
                _uiState.value = UserUiState(status = RequestStatus.SUCCESS)
                return true;
            } else {
                _uiState.value = UserUiState(status = RequestStatus.ERROR, error = ErrorUtils.parseError(response = data))
            }

        } catch (
            e: Exception
        ) {
            _uiState.value = UserUiState(status = RequestStatus.ERROR, error = e.message)
        }

        return false;
    }

    fun isLoading() = uiState.value.status == RequestStatus.LOADING

    suspend fun delete() {
        _uiState.value = UserUiState(status = RequestStatus.LOADING)
        try {
            val data = userRepository.delete()

            if (data.isSuccessful) {
                _uiState.value = UserUiState(status = RequestStatus.SUCCESS)
            } else {
                _uiState.value = UserUiState(status = RequestStatus.ERROR, error = ErrorUtils.parseError(response = data))
            }

        } catch (
            e: Exception
        ) {
            _uiState.value = UserUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun changePassword(newPassword: String, repeatPassword: String) {
        _uiState.value = UserUiState(status = RequestStatus.LOADING)
        try {
            if (newPassword != repeatPassword) {
                _uiState.value = UserUiState(status = RequestStatus.ERROR, error = "As senhas não coincidem")
                return
            }

            val passwordCodeDTO = PasswordCodeDTO()
            passwordCodeDTO.password = newPassword
            val data = userRepository.changePasswordAuth(passwordCodeDTO)

            if (data.isSuccessful) {
                _uiState.value = UserUiState(status = RequestStatus.SUCCESS)
            } else {
                _uiState.value = UserUiState(status = RequestStatus.ERROR, error = ErrorUtils.parseError(response = data))
            }

        } catch (
            e: Exception
        ) {
            _uiState.value = UserUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }
}