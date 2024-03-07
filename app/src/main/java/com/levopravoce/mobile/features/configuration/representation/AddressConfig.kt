package com.levopravoce.mobile.features.configuration.representation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun AddressConfig(
    mainViewModel: MainViewModel = hiltSharedViewModel()
) {

    val authState = mainViewModel.authUiStateStateFlow.collectAsState()

    Column(Modifier.padding(20.dp)) {
        Text(
            text = "Endereço",
            color = MaterialTheme.customColorsShema.listTitle,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 28.dp)
        )
        ListElement(
            resourceId = R.drawable.address_icon,
            text = authState.value.data?.street ?: "Endereço não encontrado"
        )

        if (mainViewModel.isDeliveryMan()) {
            Column (Modifier.padding(top = 8.dp)) {
                ListElement(
                    resourceId = R.drawable.vehicle_icon,
                    text = authState.value.data?.vehicle?.model ?: "Veículo não encontrado",
                    modifier = Modifier.padding(start = 1.dp)
                )
            }
        }
    }
}

@Composable
private fun ListElement(
    @DrawableRes resourceId: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Row {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = text,
            colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.border),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(end = 12.dp)
                .then(modifier)
        )
        Text(
            text = text,
            color = MaterialTheme.customColorsShema.title,
        )
    }
}


@Composable
@Preview
private fun AddressConfigPreview() {
    MobileTheme(darkTheme = true) {
        AddressConfig()
    }
}