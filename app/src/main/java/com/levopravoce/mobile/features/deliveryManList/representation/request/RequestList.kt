package com.levopravoce.mobile.features.deliveryManList.representation.request

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.deliveryManList.domain.DeliveryManViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun RequestList(
    deliveryManViewModel: DeliveryManViewModel = hiltSharedViewModel(),
    onBackButton: (() -> Unit)? = null
) {
    val requestUiStateState = deliveryManViewModel.uiStateRequestList.collectAsState()
    val assignedRequestState = deliveryManViewModel.assignedRequest.collectAsState()

    LaunchedEffect(Unit) {
        deliveryManViewModel.findAllRequest()
    }

    val showError = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(assignedRequestState.value) {
        if (assignedRequestState.value != null) {
            when (assignedRequestState.value) {
                is ApiResponse.Error -> {
                    showError.value = true
                }
                else -> {}
            }
        }
    }

    if (assignedRequestState.value is ApiResponse.Error<RequestStatus>) {
        Alert(
            show = showError,
            message = (assignedRequestState.value as ApiResponse.Error<RequestStatus>).message
        )
    }

    Screen(padding = 0.dp) {
        Header(
            horizontal = Alignment.CenterHorizontally,
        ) {
            Row {
                BackButton(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                ) {
                    if (onBackButton != null) {
                        onBackButton()
                    }
                }
                Text(
                    text = "Solicitações",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
        LazyColumn {
            items(requestUiStateState.value.list.size) { index ->
                RequestListItem(requestUiStateState.value.list[index], deliveryManViewModel)
//                if (index !== uiState.value.list.size - 1) {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.customColorsShema.placeholder
                )
//                }
            }
        }
    }
}