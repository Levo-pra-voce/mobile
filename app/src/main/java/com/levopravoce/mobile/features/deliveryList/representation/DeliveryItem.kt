package com.levopravoce.mobile.features.deliveryList.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.dto.OrderStatus
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DeliveryItem(deliveryDTO: OrderDTO) {
    val navController = navControllerContext.current
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
                text = deliveryDTO.averageRating?.toString() ?: "Avaliação não encontrado",
                color = MaterialTheme.customColorsShema.title,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Column(modifier = Modifier.padding(start = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(text = "Detalhes") {
                navController?.navigate("delivery/${deliveryDTO.id}")
            }
            val currentDate = LocalDate.now();
            val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            val formattedDate = currentDate.format(pattern);
            if (deliveryDTO.deliveryDate != null && deliveryDTO.deliveryDate == formattedDate && deliveryDTO.status == OrderStatus.ESPERANDO) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Button(
                        text = "Iniciar",
                        modifier = Modifier.background(Color(3,139,0)),
                        padding = 8
                    ) {

                    }
                }
            }
        }
    }
}