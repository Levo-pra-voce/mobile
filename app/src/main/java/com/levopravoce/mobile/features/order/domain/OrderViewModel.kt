package com.levopravoce.mobile.features.order.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.error.ErrorUtils
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class OrderUiState(
    val orderDTO: OrderDTO? = null,
    val users: List<UserDTO>? = null,
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
) : ViewModel(){

    private val _uiState = MutableStateFlow(OrderUiState())

    val uiState: StateFlow<OrderUiState> = _uiState

    private fun setLoadState(){
        _uiState.value = OrderUiState(status = RequestStatus.LOADING)
    }

    private fun setErrorState(e: Exception){
        Log.e("OrderViewModel", "Error: ${e.message}", e)
        _uiState.value = OrderUiState(status = RequestStatus.ERROR)
    }

    suspend fun createOrder(order: OrderDTO){
        try {
            setLoadState()
            val data = orderRepository.requestOrder(order)

            if (data.isSuccessful) {
                val response = data.body() ?: throw Exception("Order not found")
                _uiState.value = OrderUiState(status = RequestStatus.SUCCESS, orderDTO = response)
            } else {
                _uiState.value = OrderUiState(status = RequestStatus.ERROR, error = ErrorUtils.parseError(response = data))
            }

        } catch (e: Exception) {
            setErrorState(e)
        }
    }

    suspend fun listDeliverymans(){
        try {
            setLoadState()
            val data = orderRepository.getAvailableDeliveryUsers()

            if (data.isSuccessful) {
                val response = data.body() ?: throw Exception("Deliverymans not found")
                _uiState.value = OrderUiState(status = RequestStatus.SUCCESS, users = response)
            } else {
                _uiState.value = OrderUiState(status = RequestStatus.ERROR, error = ErrorUtils.parseError(response = data))
            }

        } catch (e: Exception) {
            Log.e("OrderViewModel", "Error in listDeliverymans", e)
            setErrorState(e)
        }
    }


    fun isLoading() = uiState.value.status == RequestStatus.LOADING
}