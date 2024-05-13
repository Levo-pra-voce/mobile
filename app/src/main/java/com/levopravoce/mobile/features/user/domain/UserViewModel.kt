package com.levopravoce.mobile.features.user.domain

import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.error.APIError
import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.auth.domain.AuthStore
import com.levopravoce.mobile.features.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

data class UserUiState(
    val data: UserDTO? = null,
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authStore: AuthStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())

    val uiState: StateFlow<UserUiState> = _uiState

    suspend fun register(userType: UserType, user: UserDTO) {
        _uiState.value = UserUiState(status = RequestStatus.LOADING)
        try {
            val data = userRepository.register(userType.name, user)

            if (data.isSuccessful) {
                val user = data.body() ?: throw Exception("User not found")
                authStore.saveToken(user.token)
                _uiState.value = UserUiState(status = RequestStatus.SUCCESS)
            } else {
                val gson = GsonBuilder().serializeNulls().create()
                val error = gson.fromJson(data.errorBody()?.string(), APIError::class.java)
                _uiState.value = UserUiState(status = RequestStatus.ERROR, error = error.message)
            }

        } catch (
            e: Exception
        ) {
            _uiState.value = UserUiState(status = RequestStatus.ERROR, error = e.message)
        }
    }

    fun isLoading() = uiState.value.status == RequestStatus.LOADING
}