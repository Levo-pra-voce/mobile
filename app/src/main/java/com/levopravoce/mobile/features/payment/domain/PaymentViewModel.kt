package com.levopravoce.mobile.features.payment.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.BuildConfig
import com.levopravoce.mobile.config.WebSocketClient
import com.levopravoce.mobile.features.auth.domain.AuthStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val authStore: AuthStore,
    ) : ViewModel() {
    val webSocketState = webSocketClient.messageFlow.asStateFlow()

    override fun onCleared() {
        webSocketClient.disconnect()
        super.onCleared()
    }

    suspend fun connectWebSocket() {
        webSocketClient.connect()
    }

    suspend fun generateQrCodeLink(): String {
        val token = authStore.getAccessToken.first()
        val apiBaseUrl = BuildConfig.API_URL;
        return "$apiBaseUrl/api/order/payment?jwt=$token"
    }
}