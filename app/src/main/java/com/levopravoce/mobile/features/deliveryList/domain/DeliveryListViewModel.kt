package com.levopravoce.mobile.features.deliveryList.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
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

@HiltViewModel
class DeliveryListViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
) : ViewModel(){

    private val _uiState = MutableStateFlow(DeliveryListUiState())

    val uiState: StateFlow<DeliveryListUiState> = _uiState

    suspend fun findAll() {
        _uiState.value = _uiState.value.copy(status = RequestStatus.LOADING)
        try {
                val list = orderRepository.findAllPending()
            _uiState.value = _uiState.value.copy(status = RequestStatus.SUCCESS, list = list)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun findById(id: String) {
        _uiState.value = _uiState.value.copy(status = RequestStatus.LOADING)
        try {
            val order = orderRepository.findById(id)
            _uiState.value = _uiState.value.copy(status = RequestStatus.SUCCESS, order = order)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(status = RequestStatus.ERROR, error = e.message)
        }
    }

    fun isLoading() = uiState.value.status == RequestStatus.LOADING
}