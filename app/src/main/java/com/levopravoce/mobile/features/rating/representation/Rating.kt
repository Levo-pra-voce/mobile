package com.levopravoce.mobile.features.rating.representation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.app.representation.Title
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.rating.domain.RatingViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch

enum class RatingEnum(val note: Int, val label: String) {
    ONE(1, "Ruim"),
    TWO(2, "Regular"),
    THREE(3, "Bom"),
    FOUR(4, "Muito bom"),
    FIVE(5, "Excelente")
}

@Composable
fun Rating(
    orderId: Long,
    ratingViewModel: RatingViewModel = hiltSharedViewModel(),
) {
    val navController = navControllerContext.current
    val coroutineScope = rememberCoroutineScope()
    var comment: String? by remember { mutableStateOf(null) }
    val showAlert = remember {
        mutableStateOf(false)
    }
    val messageAlert = remember {
        mutableStateOf("")
    }
    val selectedRating = remember { mutableStateOf(RatingEnum.FIVE) }
    val redirectToHome = {
        navController?.navigate(
            Routes.Home.INICIAL
        ) {
            popUpTo(Routes.Home.CLIENT_PAYMENT) {
                inclusive = true
            }
            popUpTo(Routes.Home.DELIVERY_TRACKING_CLIENT) {
                inclusive = true
            }
        }
    }
    Alert(show = showAlert, message = messageAlert.value)
    Screen {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Title(text = "Avaliação")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                PersonIcon()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Gostaria de deixar uma avaliação para o entregador?",
                textAlign = TextAlign.Center,
                color = MaterialTheme.customColorsShema.title
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = comment ?: "",
                onValueChange = { comment = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(10.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.customColorsShema.border,
                        shape = RoundedCornerShape(8.dp)
                    ),
                placeholder = {
                    Text(
                        text = "Deixe seu comentário",
                        color = MaterialTheme.customColorsShema.placeholder
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRating.value == RatingEnum.ONE,
                        onClick = { selectedRating.value = RatingEnum.ONE },
                    )
                    Text(text = RatingEnum.ONE.label, color = MaterialTheme.customColorsShema.title)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRating.value == RatingEnum.TWO,
                        onClick = { selectedRating.value = RatingEnum.TWO },
                    )
                    Text(text = RatingEnum.TWO.label, color = MaterialTheme.customColorsShema.title)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRating.value == RatingEnum.THREE,
                        onClick = { selectedRating.value = RatingEnum.THREE },
                    )
                    Text(text = RatingEnum.THREE.label, color = MaterialTheme.customColorsShema.title)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRating.value == RatingEnum.FOUR,
                        onClick = { selectedRating.value = RatingEnum.FOUR },
                    )
                    Text(text = RatingEnum.FOUR.label, color = MaterialTheme.customColorsShema.title)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRating.value == RatingEnum.FIVE,
                        onClick = { selectedRating.value = RatingEnum.FIVE },
                    )
                    Text(text = RatingEnum.FIVE.label, color = MaterialTheme.customColorsShema.title)
                }
            }
            Row(modifier = Modifier.fillMaxHeight().fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Button(text = "Voltar", modifier = Modifier.width(100.dp)) {
                    redirectToHome()
                }
                Button(text = "Enviar", modifier = Modifier.width(100.dp), backgroundColor = MaterialTheme.customColorsShema.success) {
                    coroutineScope.launch {
                        val comment = comment
                        if (comment.isNullOrBlank()) {
                            showAlert.value = true
                            messageAlert.value = "Comentário é obrigatório"
                            return@launch
                        }
                        ratingViewModel.sendReview(
                            orderId = orderId,
                            rating = selectedRating.value.note,
                            comment = comment
                        )
                        redirectToHome()
                    }
                }
            }
        }
    }

}