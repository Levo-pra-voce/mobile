package com.levopravoce.mobile.features.order.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.error.ErrorUtils
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.OrderRepository
import com.levopravoce.mobile.features.user.domain.UserUiState
import com.levopravoce.mobile.features.order.data.dto.RecommendUserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class OrderUiState(
    val orderDTO: OrderDTO? = null,
    val status: RequestStatus = RequestStatus.IDLE,
    val error: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState
    private val _usersList = MutableStateFlow<List<RecommendUserDTO>>(listOf())
    val usersList: StateFlow<List<RecommendUserDTO>> = _usersList

    private fun setLoadState() {
        _uiState.value = OrderUiState(status = RequestStatus.LOADING)
    }

    private fun setErrorState(e: Exception) {
        Log.e("OrderViewModel", "Error: ${e.message}", e)
        _uiState.value = OrderUiState(status = RequestStatus.ERROR)
    }

    suspend fun createOrder(order: OrderDTO): Boolean {
        try {
            setLoadState()
            val data = orderRepository.requestOrder(order)

            if (data.isSuccessful) {
                val response = data.body() ?: throw Exception("Order not found")
                _uiState.value = OrderUiState(status = RequestStatus.SUCCESS, orderDTO = response)
                return true
            } else {
                _uiState.value = OrderUiState(
                    status = RequestStatus.ERROR,
                    error = ErrorUtils.parseError(response = data)
                )
                return false
            }
        } catch (e: Exception) {
            _uiState.value = OrderUiState(status = RequestStatus.ERROR, error = e.message)
            return false
        }
    }

    fun validateOrderFields(order: OrderDTO): Boolean {
       if(order.height == null || order.width == null || order.maxWeight == null || order.deliveryDate.isNullOrEmpty()) {
           _uiState.value = OrderUiState(status = RequestStatus.ERROR, error = "Campos preenchidos incorretamente")
           return false
       }
        return true
    }

    fun validateDeliveryDate(order: OrderDTO): Boolean {
        val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        val deliveryDate: LocalDate = LocalDate.parse(order.deliveryDate, pattern);
        val currentDate = LocalDate.now();
        return if(deliveryDate.isBefore(currentDate) && !deliveryDate.equals(currentDate)) {
            _uiState.value = OrderUiState(status = RequestStatus.ERROR, error = "Data de entrega inválida")
            false
        } else {
            true
        }
    }

    suspend fun getCurrentOrderInPending(): OrderDTO? {
        val response = orderRepository.getLastPending()
        if (response.isSuccessful) {
            val orderResponse = response.body();
            return orderResponse
        }
        return null;
    }

    suspend fun getAvailableDeliveryUsers(): List<RecommendUserDTO> {
        val response = orderRepository.getAvailableDeliveryUsers()
        if (response.isSuccessful) {
            val userDTOS = response.body();
            _usersList.value = userDTOS ?: listOf()
            return userDTOS ?: listOf()
        }
        return listOf();
    }

    fun isLoading() = uiState.value.status == RequestStatus.LOADING

    suspend fun assignDeliveryman(userId: Long): String {
        try {
            setLoadState()
            val data = orderRepository.assignDeliveryman(userId)

            if (data.isSuccessful) {
                _uiState.value = OrderUiState(status = RequestStatus.SUCCESS)
                return "Entregador atribuído com sucesso!"
            } else {
                _uiState.value = OrderUiState(
                    status = RequestStatus.ERROR,
                    error = ErrorUtils.parseError(response = data)
                )
            }
        } catch (e: Exception) {
            setErrorState(e)
        }
        return uiState.value.error ?: "Erro ao atribuir entregador"
    }
}