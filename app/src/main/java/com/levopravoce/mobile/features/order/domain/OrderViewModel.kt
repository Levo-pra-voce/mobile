package com.levopravoce.mobile.features.order.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class OrderUiState(
    val orderDTO: OrderDTO? = null,
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
) : ViewModel(){

    private val _uiState = MutableStateFlow(OrderUiState())

    val uiState: StateFlow<OrderUiState> = _uiState

    suspend fun createOrder(order: OrderDTO){
        _uiState.value = OrderUiState(status = RequestStatus.LOADING)
        try {
            val data = orderRepository.requestOrder(order)

            if (data.isSuccessful) {
                val response = data.body() ?: throw Exception("Order not found")
                _uiState.value = OrderUiState(status = RequestStatus.SUCCESS, orderDTO = response)
            } else {
                _uiState.value = OrderUiState(status = RequestStatus.ERROR)
            }

        } catch (e: Exception) {
            _uiState.value = OrderUiState(status = RequestStatus.ERROR)
        }
    }


    fun isLoading() = uiState.value.status == RequestStatus.LOADING
}