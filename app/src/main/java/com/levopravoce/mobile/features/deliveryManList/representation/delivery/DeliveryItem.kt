package com.levopravoce.mobile.features.deliveryManList.representation.delivery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.levopravoce.mobile.features.deliveryManList.domain.DeliveryManViewModel
import com.levopravoce.mobile.features.deliveryManList.representation.DeliveryManView
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.dto.OrderStatus
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DeliveryItem(deliveryDTO: OrderDTO, deliveryManViewModel: DeliveryManViewModel) {
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
                text = deliveryDTO.client?.name ?: "Nome não encontrado",
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = deliveryDTO.averageRating?.toString()?.replace(".", ",") ?: "Avaliação não encontrado",
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Column(
            modifier = Modifier.padding(start = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(text = "Detalhes") {
                navController?.navigate("delivery/${deliveryDTO.id}")
            }
            if (deliveryDTO.deliveryDate != null) {
                val currentDate = LocalDate.now()
                val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val localDateByOrder = LocalDate.parse(deliveryDTO.deliveryDate, pattern)
                if (deliveryDTO.status == OrderStatus.ACEITO && (currentDate.isAfter(
                        localDateByOrder
                    ) || currentDate.isEqual(localDateByOrder))
                ) {
                    val showSuccessAlert = remember { mutableStateOf(false) }
                    Alert(show = showSuccessAlert, message = "Iniciando entrega") {
                        navController?.navigateUp()
                    }
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Button(
                            text = "Iniciar",
                            modifier = Modifier.background(Color(3, 139, 0)),
                            padding = 8
                        ) {
                            val deliveryId = deliveryDTO.id
                            if (deliveryId != null) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    val apiResponse = deliveryManViewModel.startOrder(deliveryId)
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