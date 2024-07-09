package com.levopravoce.mobile.features.deliveryManList.representation.request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.deliveryManList.data.dto.RequestDTO
import com.levopravoce.mobile.features.deliveryManList.domain.DeliveryManViewModel
import com.levopravoce.mobile.features.deliveryManList.representation.DeliveryManView
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun RequestListItem(
    requestDTO: RequestDTO,
    deliveryManViewModel: DeliveryManViewModel,
    deliveryManViewState: MutableState<DeliveryManView?>
) {
    val navController = navControllerContext.current
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PersonIcon()
            Text(
                text = requestDTO.name ?: "Nome não encontrado",
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = requestDTO.averageRating?.toString()?.replace(".", ",")
                    ?: "Avaliação não encontrado",
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Column(
            modifier = Modifier.padding(start = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(text = "Detalhes") {
                navController?.navigate("request/${requestDTO.orderId}")
            }
            val deliveryDate = requestDTO.deliveryDate
            if (deliveryDate != null) {
                val currentDate = LocalDate.now();
                val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                val parsedDeliveryDate = LocalDate.parse(deliveryDate, pattern)
                if (currentDate.isAfter(parsedDeliveryDate) || currentDate.isEqual(
                        parsedDeliveryDate
                    )
                ) {
                    val showSuccessAlert = remember { mutableStateOf(false) }
                    Alert(show = showSuccessAlert, message = "Pedido foi aceito") {
                        coroutineScope.launch(Dispatchers.Main) {
                            deliveryManViewState.value = DeliveryManView.DELiVERY
                        }
                    }
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Button(
                            text = "Aceitar",
                            modifier = Modifier.background(Color(3, 139, 0)),
                            padding = 8
                        ) {
                            val orderId = requestDTO.orderId
                            if (orderId != null) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    val apiResponse = deliveryManViewModel.acceptRequest(orderId)
                                    if (apiResponse is ApiResponse.Success) {
                                        showSuccessAlert.value = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}