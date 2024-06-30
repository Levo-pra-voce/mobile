package com.levopravoce.mobile.features.tracking.domain

import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.android.gms.maps.model.LatLng
import com.levopravoce.mobile.config.Destination
import com.levopravoce.mobile.config.WebSocketClient
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.order.data.OrderRepository
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.tracking.data.OrderTrackingDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val locationClient: ILocationService,
    private val webSocketClient: WebSocketClient
) : ViewModel() {
    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()
    val webSocketState = webSocketClient.messageFlow.asStateFlow()
    val orderState = MutableStateFlow<OrderDTO?>(null)
    val firstRender = MutableStateFlow(true)
    val currentTrackingState = MutableStateFlow<ApiResponse<OrderDTO?>>(ApiResponse.Loading())

    override fun onCleared() {
        webSocketClient.disconnect()
        super.onCleared()
    }

    suspend fun connectWebSocket() {
        webSocketClient.connect()
    }

    fun sendMessage(trackingDTO: OrderTrackingDTO) {
        webSocketClient.send(Destination.ORDER_MAP, trackingDTO)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    suspend fun handlePermissionEvent(event: PermissionStatus) {
        when (event) {
            is PermissionStatus.Granted -> {
                locationClient.requestLocationUpdates().collect { location ->
                    _viewState.value = ViewState.Success(location)
                }
            }

            is PermissionStatus.Denied -> {
                _viewState.value = ViewState.RevokedPermissions
            }
        }
    }

    suspend fun setStateCurrentTracking() {
        currentTrackingState.value = ApiResponse.Loading()
        val response = orderRepository.getLastProgress()
        if (response.isSuccessful) {
            val orderResponse = response.body();
            orderState.value = orderResponse
            currentTrackingState.value = ApiResponse.Success(orderResponse)
        } else {
            currentTrackingState.value = ApiResponse.Error("Erro ao buscar pedido")
        }
    }

    suspend fun getCurrentOrderInProgress(): OrderDTO? {
        val response = orderRepository.getLastProgress()
        if (response.isSuccessful) {
            val orderResponse = response.body();
            orderState.value = orderResponse
            return orderResponse
        }
        return null;
    }

    suspend fun finishOrder() {
        val response = orderRepository.finishOrder()
        if (response.isSuccessful) {
            setStateCurrentTracking()
        }
    }

    fun setFirstRender() {
        firstRender.value = false
    }
}

sealed interface ViewState {
    object Loading : ViewState
    data class Success(val location: LatLng?) : ViewState
    object RevokedPermissions : ViewState
}