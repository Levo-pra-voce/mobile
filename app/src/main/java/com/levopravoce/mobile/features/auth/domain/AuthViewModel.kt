package com.levopravoce.mobile.features.auth.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.features.auth.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


data class AuthUiState(
    val test: String = ""
)
private val _uiState = MutableStateFlow(AuthUiState())
val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

@Singleton
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    lateinit var uiState: AuthUiState
    init {
        _uiState.value = AuthUiState()
    }

    fun meRequest() {
        _uiState.value = AuthUiState()
    }
}