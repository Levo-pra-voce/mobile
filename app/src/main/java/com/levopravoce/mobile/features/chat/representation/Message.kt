package com.levopravoce.mobile.features.chat.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.features.app.data.dto.MessageType
import com.levopravoce.mobile.features.chat.data.entity.Message
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Message(
    message: Message,
    isCurrentUser: Boolean
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment =
        when (isCurrentUser) {
            true -> Alignment.End
            false -> Alignment.Start
        }
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.customColorsShema.border, shape = RoundedCornerShape(12.dp))
                .padding(6.dp)
        ) {
            Text(
                text = message.text ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.customColorsShema.background,
            )
        }
    }
}

@Preview
@Composable
fun MessagePreview() {
    MobileTheme(darkTheme = true) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.customColorsShema.background)) {
            Message(
                message = Message(
                    id = 1,
                    text = "Hello, world!",
                    channelId = 1,
                    type = MessageType.TEXT,
                    date = java.sql.Date(System.currentTimeMillis()),
                    imagePath = null,
                    sender = "user"
                ),
                isCurrentUser = true
            )
        }
    }
}