package com.levopravoce.mobile.features.deliveryManList.representation.request

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.deliveryManList.domain.DeliveryManViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RequestDetails(
    deliveryId: String,
    deliveryManViewModel: DeliveryManViewModel = hiltSharedViewModel(),
) {
    val uiState = deliveryManViewModel.uiStateDelivery.collectAsState()
    val acceptRequestState = deliveryManViewModel.acceptedOrderRequest.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val navController = navControllerContext.current

    LaunchedEffect(Unit) {
        deliveryManViewModel.findById(deliveryId)
    }

    val showError = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(acceptRequestState.value) {
        if (acceptRequestState.value != null) {
            when (acceptRequestState.value) {
                is ApiResponse.Error -> {
                    showError.value = true
                }

                else -> {}
            }
        }
    }

    if (acceptRequestState.value is ApiResponse.Error<RequestStatus>) {
        Alert(
            show = showError,
            message = (acceptRequestState.value as ApiResponse.Error<RequestStatus>).message
        )
    }


    Screen(Modifier.verticalScroll(rememberScrollState())) {
        BackButton(
            Modifier.size(28.dp)
        ) {
            navController?.navigate(
                Routes.Home.DELIVERY_MAN.replace("{screen}", "REQUEST")
            ) {
                popUpTo(Routes.Home.DELIVERY_MAN.replace("{screen}", "REQUEST")) {
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
                text = uiState.value.order?.averageRating.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 42.dp)
            )
        }
        Column(Modifier.padding(top = 32.dp)) {
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

        Column(Modifier.padding(top = 24.dp)) {
            Button(
                text = "Aceitar", modifier = Modifier.fillMaxWidth(), backgroundColor =
                Color(0xFF038B00)
            ) {
                val orderId = uiState.value.order?.id
                if (orderId != null) {
                    coroutineScope.launch(Dispatchers.IO) {
                        val apiResponse = deliveryManViewModel.acceptRequest(orderId)
                        if (apiResponse is ApiResponse.Success) {
                            withContext(Dispatchers.Main) {
                                navController?.navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }
}