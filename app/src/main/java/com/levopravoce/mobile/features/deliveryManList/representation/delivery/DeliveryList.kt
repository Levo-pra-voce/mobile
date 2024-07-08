package com.levopravoce.mobile.features.deliveryManList.representation.delivery

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.deliveryManList.domain.DeliveryManViewModel
import com.levopravoce.mobile.features.deliveryManList.representation.DeliveryManView
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun DeliveryList(
    deliveryManViewModel: DeliveryManViewModel = hiltSharedViewModel(),
    onBackButton: (() -> Unit)? = null
) {
    val uiState = deliveryManViewModel.uiStateDelivery.collectAsState()

    LaunchedEffect(Unit) {
        deliveryManViewModel.findAllDelivery()
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
                    text = "Suas entregas",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
        LazyColumn {
            items(uiState.value.list.size) { index ->
                DeliveryItem(uiState.value.list[index], deliveryManViewModel)
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.customColorsShema.placeholder
                )
            }
        }
    }
}