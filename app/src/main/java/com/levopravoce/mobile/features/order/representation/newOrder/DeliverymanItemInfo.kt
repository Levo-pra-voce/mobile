package com.levopravoce.mobile.features.order.representation.newOrder

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.levopravoce.mobile.R
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.order.data.dto.RatingDTO
import com.levopravoce.mobile.features.order.data.dto.RecommendUserDTO
import com.levopravoce.mobile.features.order.domain.OrderViewModel
import com.levopravoce.mobile.features.user.representation.formatPrice
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
    var showRatingDialog by remember {
        mutableStateOf(false)
    }
    val showModal = {
        showRatingDialog = true
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        if (showRatingDialog) {
            RatingDialog(
                ratings = recommendUserDTO.ratings ?: listOf()
            ) {
                showRatingDialog = false
            }
        }
        Alert(show = showAlert, message = messageAlert.value)
        Column(modifier = Modifier.clickable {
            showModal()
        }) {
            PersonIcon()
        }
        Column(
            Modifier.padding(top = 16.dp, start = 8.dp)
        ) {
            Row(modifier = Modifier.clickable {
                showModal()
            }) {
                Text(
                    text = recommendUserDTO.name ?: "",
                    color = MaterialTheme.customColorsShema.placeholder
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = recommendUserDTO.averageRating.toString().replace(".", ","),
                    color = MaterialTheme.customColorsShema.title
                )
            }
            Row(modifier = Modifier.clickable {
                showModal()
            }) {
                Text(text = "Valor: ", color = MaterialTheme.customColorsShema.title)
                Text(
                    text = "RS ${formatPrice(recommendUserDTO.price)}",
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
            colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.border),
            modifier = Modifier
                .padding(start = 16.dp, top = 48.dp)
                .clickable {
                    val url =
                        "https://wa.me/${recommendUserDTO.phone}?text=Olá, gostaria de solicitar um serviço de entrega"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse(url)
                    context.startActivity(intent)
                }
        )
    }
}

@Composable
fun RatingDialog(
    ratings: List<RatingDTO>,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Avaliações",
                    color = MaterialTheme.customColorsShema.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            ) {
                for (rating in ratings) {
                    Column {
                        Text(text = rating.creationDate ?: "")
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(text = "Avaliação: ${formatComment(rating.comment ?: "")}")
                            Text(text = "Nota: ${formatPrice(rating.note?.toInt())}")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

fun formatComment(comment: String): String {
    if (comment.isEmpty()) return "Sem comentários"

    return if (comment.length > 50) {
        comment.substring(0, 50) + "..."
    } else {
        comment
    }
}
