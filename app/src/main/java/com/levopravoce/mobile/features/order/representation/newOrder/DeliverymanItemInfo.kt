package com.levopravoce.mobile.features.order.representation.newOrder

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.R
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.order.data.dto.RecommendUserDTO
import com.levopravoce.mobile.features.order.domain.OrderViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun DeliverymanItemInfo(
    recommendUserDTO: RecommendUserDTO = RecommendUserDTO(),
    orderViewModel: OrderViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val showAlert = remember {
        mutableStateOf(false)
    }
    val messageAlert = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Alert(show = showAlert, message = messageAlert.value)
        PersonIcon()
        Column(
            Modifier.padding(top = 16.dp, start = 8.dp)
        ) {
            Row {
                Text(
                    text = recommendUserDTO.name ?: "",
                    color = MaterialTheme.customColorsShema.placeholder
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = recommendUserDTO.averageRating.toString(),
                    color = MaterialTheme.customColorsShema.title
                )
            }
            Row {
                Text(text = "Valor: ", color = MaterialTheme.customColorsShema.title)
                Text(
                    text = "RS ${recommendUserDTO.price.toString()}",
                    color = MaterialTheme.customColorsShema.title
                )
            }
            Row(Modifier.padding(top = 12.dp)) {
                Button(
                    text = "Solicitar", modifier = Modifier
                        .width(128.dp)
                ) {
                    coroutineScope.launch(Dispatchers.IO) {
                        val message = orderViewModel.assignDeliveryman(recommendUserDTO.userId ?: 0)
                        showAlert.value = true
                        messageAlert.value = message
                    }
                }
            }
        }
        Image(
            painter = painterResource(R.drawable.whatsapp_icon),
            contentDescription = "icone do whatsapp",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(start = 16.dp, top = 48.dp)
                .clickable {
                    val url = "https://wa.me/${recommendUserDTO.phone}?text=Olá, gostaria de solicitar um serviço de entrega"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse(url)
                    context.startActivity(intent)
                }
        )
    }
}
