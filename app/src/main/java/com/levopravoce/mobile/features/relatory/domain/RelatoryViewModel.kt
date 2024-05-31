package com.levopravoce.mobile.features.relatory.domain

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.relatory.data.RelatoryRepository
import com.levopravoce.mobile.features.relatory.data.dto.RelatoryDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.time.LocalDate
import javax.inject.Inject

data class RepresentationUiState(
    val status: RequestStatus = RequestStatus.IDLE,
    val relatories: List<RelatoryDTO> = emptyList(),
    val relatoryDTO: RelatoryDTO? = null,
    val error: String? = null
)

@HiltViewModel
class RelatoryViewModel @Inject constructor(
    private val relatoryRepository: RelatoryRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
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
                _uiState.value = RepresentationUiState(
                    status = RequestStatus.SUCCESS,
                    relatories = response.content
                )
            } else {
                _uiState.value = RepresentationUiState(status = RequestStatus.ERROR)
            }

        } catch (e: Exception) {
            _uiState.value = this.uiState.value.copy(status = RequestStatus.ERROR, error = e.message)
        }
    }

    suspend fun getRelatoryXlsx(
        deliveryDate: LocalDate? = null,
    ) {
        _uiState.value = _uiState.value.copy(status = RequestStatus.LOADING)
        try {
            withContext(Dispatchers.IO) {
                val data = relatoryRepository.getRelatoriesXlsx(deliveryDate)
                val fileOutputStream =
                    context.openFileOutput("relatories.xlsx", Context.MODE_PRIVATE)
                fileOutputStream.write(data.bytes())
                fileOutputStream.close()
                val reportFile = File(context.filesDir, "relatories.xlsx")
                val uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    reportFile
                )
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(
                    uri,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(intent)
            }
        } catch (exception: Exception) {
            if (exception is ActivityNotFoundException) {
                _uiState.value = this._uiState.value.copy(status = RequestStatus.ERROR, error = "Baixe um aplicativo para abrir arquivos Excel")
                return
            }
        }
    }

    fun isLoading() = uiState.value.status == RequestStatus.LOADING
}