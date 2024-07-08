package com.levopravoce.mobile.features.payment.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levopravoce.mobile.BuildConfig
import com.levopravoce.mobile.config.WebSocketClient
import com.levopravoce.mobile.features.auth.domain.AuthStore
import com.levopravoce.mobile.features.order.data.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val authStore: AuthStore,
    private val orderRepository: OrderRepository
    ) : ViewModel() {
    val webSocketState = webSocketClient.messageFlow.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            connectWebSocket()
        }
    }

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

    suspend fun sendPaymentRequest(): Response<Unit> {
        return orderRepository.sendPaymentRequest()
    }
}