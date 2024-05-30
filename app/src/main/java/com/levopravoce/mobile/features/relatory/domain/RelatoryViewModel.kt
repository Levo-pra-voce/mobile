package com.levopravoce.mobile.features.relatory.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.relatory.data.RelatoryRepository
import com.levopravoce.mobile.features.relatory.data.dto.RelatoryDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

data class RepresentationUiState(
    val status: RequestStatus = RequestStatus.IDLE,
    val relatories: List<RelatoryDTO> = emptyList(),
    val relatoryDTO: RelatoryDTO? = null
)

@HiltViewModel
class RelatoryViewModel @Inject constructor(
    private val relatoryRepository: RelatoryRepository,
) : ViewModel(){
    private val _uiState = MutableStateFlow(RepresentationUiState())

    val uiState: StateFlow<RepresentationUiState> = _uiState

    suspend fun getRelatories(
        deliveryDate: LocalDate? = null,
    ) {
        _uiState.value = RepresentationUiState(status = RequestStatus.LOADING)
        try {
            val data = relatoryRepository.getRelatories(deliveryDate)

            if (data.isSuccessful) {
                val response = data.body() ?: throw Exception("Relatories not found")
                _uiState.value = RepresentationUiState(status = RequestStatus.SUCCESS, relatories = response.content)
            } else {
                _uiState.value = RepresentationUiState(status = RequestStatus.ERROR, )
            }

        } catch (e: Exception) {
            _uiState.value = RepresentationUiState(status = RequestStatus.ERROR)
        }
    }

    fun isLoading() = uiState.value.status == RequestStatus.LOADING
}