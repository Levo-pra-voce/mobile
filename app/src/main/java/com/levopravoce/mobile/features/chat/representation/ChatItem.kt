package com.levopravoce.mobile.features.chat.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.R
import com.levopravoce.mobile.features.chat.data.dto.ChatUserDTO
import com.levopravoce.mobile.features.start.representation.bottomBorder
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun ChatItem(
    chatUserDTO: ChatUserDTO
) {
    val navController = navControllerContext.current

    Row(
        Modifier
            .padding(top = 24.dp)
            .clickable {
                navController?.navigate(Routes.Home.MESSAGES.substringBefore('/') + "/${chatUserDTO.channelId}")
            }) {
        Image(
            painter = painterResource(R.drawable.person_icon),
            contentDescription = "icone da pessoa",
            contentScale = ContentScale.FillHeight,
        )
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .bottomBorder(1.dp, MaterialTheme.customColorsShema.border)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = chatUserDTO.name ?: "",
                    color = MaterialTheme.customColorsShema.title,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChatItemPreview() {
    ChatItem(
        chatUserDTO = ChatUserDTO(
            name = "Nome da pessoa",
            channelId = 1L
        )
    )
}