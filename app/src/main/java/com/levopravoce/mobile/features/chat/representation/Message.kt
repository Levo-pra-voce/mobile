package com.levopravoce.mobile.features.chat.representation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import com.levopravoce.mobile.features.chat.data.entity.Message

@Composable
fun Message(
    message: Message
) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = message.text ?: "dapklçç~dlaaldlk,as,l",
            style = MaterialTheme.typography.titleMedium
        )
    }
}