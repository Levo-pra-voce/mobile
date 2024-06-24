package com.levopravoce.mobile.features.payment.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.config.Destination
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.app.representation.Title
import com.levopravoce.mobile.features.payment.data.OrderPaymentDTO
import com.levopravoce.mobile.features.payment.domain.PaymentViewModel
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun DeliveryPayment(
    paymentViewModel: PaymentViewModel = hiltSharedViewModel()
) {
    val messageState by paymentViewModel.webSocketState.collectAsState()
    val navController = navControllerContext.current
    val isPaid = remember {
        mutableStateOf(false)
    }
    val isAlertVisible = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        paymentViewModel.connectWebSocket()
    }
    LaunchedEffect(messageState) {
        if (messageState?.destination == Destination.ORDER_PAYMENT) {
            val gson = Gson()
            val orderPaymentDTO = gson.fromJson(messageState?.message, OrderPaymentDTO::class.java)
            isPaid.value = orderPaymentDTO.isPaid
            isAlertVisible.value = true
        }
    }
    Alert(show = isAlertVisible, message = "Pagamento realizado com sucesso!")
    Screen(padding = 32.dp) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Title(text = "Pagamento")
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isPaid.value) {
                    Image(
                        painter = painterResource(R.drawable.like),
                        contentDescription = "icone de gostei",
                        modifier = Modifier.padding(bottom = 64.dp),
                        contentScale = ContentScale.FillHeight,
                    )
                    Text(
                        text = "Seu pagamento foi realizado",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.customColorsShema.title,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.question_mark),
                        contentDescription = "icone de interrogação",
                        modifier = Modifier.padding(bottom = 64.dp),
                        contentScale = ContentScale.FillHeight,
                    )
                    Text(
                        text = "Pagamento ainda não realizado",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.customColorsShema.title,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Button(text = "Voltar", modifier = Modifier.fillMaxWidth()) {
                navController?.popBackStack()
            }
        }
    }
}