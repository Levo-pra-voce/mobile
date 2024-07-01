package com.levopravoce.mobile.features.deliveryManList.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.deliveryManList.data.dto.RequestDTO
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class DeliveryListUiState(
    val status: RequestStatus = RequestStatus.IDLE,
    val list: List<OrderDTO> = emptyList(),
    val order: OrderDTO? = null,
    val error: String? = null
)

data class DeliveryRequestUiState(
    val status: RequestStatus = RequestStatus.IDLE,
    val list: List<RequestDTO> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DeliveryManViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
) : ViewModel() {

    private val _uiStateDelivery = MutableStateFlow(DeliveryListUiState())
    val uiStateDelivery: StateFlow<DeliveryListUiState> = _uiStateDelivery

    private val _uiStateRequest = MutableStateFlow(DeliveryRequestUiState())
    val uiStateRequestList: StateFlow<DeliveryRequestUiState> = _uiStateRequest

    private val _assignedRequest: MutableStateFlow<ApiResponse<RequestStatus>?> = MutableStateFlow(null)
    val assignedRequest: StateFlow<ApiResponse<RequestStatus>?> = _assignedRequest

    private val _acceptedRequestRequest: MutableStateFlow<ApiResponse<RequestStatus>?> = MutableStateFlow(null)
    val acceptedOrderRequest: StateFlow<ApiResponse<RequestStatus>?> = _acceptedRequestRequest

    private val _startOrderRequest: MutableStateFlow<ApiResponse<Unit>?> = MutableStateFlow(null)
    val startOrderRequest: StateFlow<ApiResponse<Unit>?> = _startOrderRequest

    suspend fun findAllDelivery() {
        _uiStateDelivery.value = _uiStateDelivery.value.copy(status = RequestStatus.LOADING)
        try {
            val list = orderRepository.findAllPending()
            _uiStateDelivery.value =
                _uiStateDelivery.value.copy(status = RequestStatus.SUCCESS, list = list)
        } catch (e: Exception) {
            _uiStateDelivery.value =
                _uiStateDelivery.value.copy(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun findAllRequest() {
        _uiStateRequest.value = _uiStateRequest.value.copy(status = RequestStatus.LOADING)
        try {
            val list = orderRepository.findAllRequest()
            _uiStateRequest.value =
                _uiStateRequest.value.copy(status = RequestStatus.SUCCESS, list = list)
        } catch (e: Exception) {
            _uiStateRequest.value =
                _uiStateRequest.value.copy(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun findById(id: String) {
        _uiStateDelivery.value = _uiStateDelivery.value.copy(status = RequestStatus.LOADING)
        try {
            val order = orderRepository.findById(id)
            _uiStateDelivery.value =
                _uiStateDelivery.value.copy(status = RequestStatus.SUCCESS, order = order)
        } catch (e: Exception) {
            _uiStateDelivery.value =
                _uiStateDelivery.value.copy(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun assignRequest(deliveryManId: Long) {
        _assignedRequest.value = ApiResponse.Loading()
        try {
            orderRepository.assignDeliveryman(deliveryManId)
            _assignedRequest.value = ApiResponse.Success(RequestStatus.SUCCESS)
        } catch (e: Exception) {
            _assignedRequest.value = ApiResponse.Error(e.message ?: "Erro ao atribuir solicitação")
        }
    }

    suspend fun acceptRequest(id: Long): ApiResponse<Unit> {
        _acceptedRequestRequest.value = ApiResponse.Loading()
        try {
            orderRepository.acceptOrder(id)
            _acceptedRequestRequest.value = ApiResponse.Success(RequestStatus.SUCCESS)
            return ApiResponse.Success(Unit)
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Erro ao aceitar solicitação"
            _acceptedRequestRequest.value = ApiResponse.Error(errorMessage)
            return ApiResponse.Error(errorMessage)
        }
    }

    suspend fun startOrder(id: Long): ApiResponse<Unit> {
        _startOrderRequest.value = ApiResponse.Loading()
        try {
            orderRepository.startOrder(id)
            _startOrderRequest.value = ApiResponse.Success(Unit)
            return ApiResponse.Success(Unit)
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Erro ao iniciar pedido"
            _startOrderRequest.value = ApiResponse.Error(errorMessage)
            return ApiResponse.Error(errorMessage)
        }
    }

    fun isLoading() = uiStateDelivery.value.status == RequestStatus.LOADING
}