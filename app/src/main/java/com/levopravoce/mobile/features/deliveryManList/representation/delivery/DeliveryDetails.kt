package com.levopravoce.mobile.features.deliveryManList.representation.delivery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.deliveryManList.domain.DeliveryManViewModel
import com.levopravoce.mobile.features.order.data.dto.OrderStatus
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DeliveryDetails(
    deliveryId: String,
    deliveryManViewModel: DeliveryManViewModel = hiltSharedViewModel(),
) {
    val uiState = deliveryManViewModel.uiStateDelivery.collectAsState()
    val navController = navControllerContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        deliveryManViewModel.findById(deliveryId)
    }

    Screen(Modifier.verticalScroll(rememberScrollState())) {
        BackButton(
            Modifier.size(28.dp)
        ) {
            navController?.navigate(
                Routes.Home.DELIVERY_MAN.replace("{screen}", "DELIVERY")
            ) {
                popUpTo(Routes.Home.DELIVERY_MAN.replace("{screen}", "DELIVERY")) {
                    inclusive = true
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Informações",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.customColorsShema.title
            )
            Column(
                modifier = Modifier.padding(top = 32.dp)
            ) {
                PersonIcon(Modifier.scale(1.5f))
            }
            Text(
                text = uiState.value.order?.averageRating.toString() ?: "0.0",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 42.dp)
            )
        }
        Column(Modifier.padding(top = 48.dp)) {
            Text(
                text = "Nome: ${uiState.value.order?.client?.name}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColorsShema.title
            )
            Text(
                text = "Data marcada: ${uiState.value.order?.deliveryDate}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Preço: R$ ${uiState.value.order?.price.toString().replace(".", ",")}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Origem: ${uiState.value.order?.originAddress}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Destino: ${uiState.value.order?.destinationAddress}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        val order = uiState.value.order
        if (order?.deliveryDate != null) {
            val currentDate = LocalDate.now()
            val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val localDateByOrder = LocalDate.parse(order.deliveryDate, pattern)
            if (order.status == OrderStatus.ACEITO && (currentDate.isAfter(
                    localDateByOrder
                ) || currentDate.isEqual(localDateByOrder))
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Button(
                        text = "Iniciar",
                        modifier = Modifier.background(Color(3, 139, 0)),
                        padding = 8
                    ) {
                        val deliveryId = order.id
                        if (deliveryId != null) {
                            coroutineScope.launch(Dispatchers.IO) {
                                val apiResponse = deliveryManViewModel.startOrder(deliveryId)
                                if (apiResponse is ApiResponse.Success) {
                                    navController?.navigateUp()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}