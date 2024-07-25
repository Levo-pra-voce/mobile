package com.levopravoce.mobile.features.order.representation.newOrder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.domain.OrderViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun DeliverymanListInfo(
    order: OrderDTO = OrderDTO(),
) {
    val orderViewModel = hiltSharedViewModel<OrderViewModel>()
    val orderViewModelState = orderViewModel.uiState.collectAsState()
    val showError = remember { mutableStateOf(false) }
    val deliveryManList = orderViewModel.usersList.collectAsState()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            orderViewModel.getAvailableDeliveryUsers()
        }
    }

    Screen(
        padding = 0.dp,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        Alert(
            show = showError,
            message = orderViewModelState.value.error ?: "Erro ao carregar entregadores"
        )
        Header(
            horizontal = Alignment.Start
        ) {
            Row {
                BackButton(
                    Modifier.scale(1.5f),
                    orderViewModel.uiState.collectAsState().value.status != RequestStatus.LOADING
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "Motoristas",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.customColorsShema.title,
                        fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                        fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
        LazyColumn {
            items(deliveryManList.value.size) {
                DeliverymanItemInfo(
                    recommendUserDTO = deliveryManList.value[it],
                    orderViewModel = orderViewModel
                )
            }
        }
    }
}
