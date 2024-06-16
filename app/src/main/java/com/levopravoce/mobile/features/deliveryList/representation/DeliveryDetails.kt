package com.levopravoce.mobile.features.deliveryList.representation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.deliveryList.domain.DeliveryListViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun DeliveryDetails(
    deliveryId: String,
    deliveryListViewModel: DeliveryListViewModel = hiltSharedViewModel()
) {
    val uiState = deliveryListViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        deliveryListViewModel.findById(deliveryId)
    }

    Screen {
        BackButton(
            Modifier.size(28.dp)
        )
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
    }
}