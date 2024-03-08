package com.levopravoce.mobile.features.chat.representation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.R
import com.levopravoce.mobile.ui.theme.customColorsShema

private const val ICON_OFFSET = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBar(
    currentMessageState: MutableState<String>
) {

    Row(
        Modifier
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.customColorsShema.button)
    ) {
        TextField(
            value = currentMessageState.value,
            onValueChange = {
                currentMessageState.value = it
            },
            placeholder = {
                Row(Modifier.padding(start = 16.dp)) {
                    Text(
                        text = "enviar mensagem",
                        color = MaterialTheme.customColorsShema.placeholder
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.customColorsShema.title,
                containerColor = MaterialTheme.customColorsShema.button,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedSupportingTextColor = Color.Red,
                focusedTrailingIconColor = Color.Red,
                focusedLabelColor = Color.Red,
                focusedLeadingIconColor = Color.Red,
            ),
            modifier = Modifier
                .width(300.dp)
        )
        Crossfade(
            targetState = currentMessageState.value.isNotEmpty(),
            label = "crossfade do botão da camera"
        ) {
            if (it) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .offset(x = ICON_OFFSET.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.send_icon),
                        contentDescription = "icone da camera",
                        contentScale = ContentScale.FillHeight,
                        colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.title),
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .offset(x = ICON_OFFSET.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.cam_icon),
                        contentDescription = "icone da camera",
                        contentScale = ContentScale.FillHeight,
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun ChatBarPreview() {
    val currentMessageState = remember {
        mutableStateOf("sdauiduaisudasioduasduasiduiasuidauiosiuoduaiosuiodauidaiuosdaisdiuadsiouiduaiuodauisduaiudasuo")
    }
    ChatBar(currentMessageState = currentMessageState)
}