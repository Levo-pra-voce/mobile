package com.levopravoce.mobile.features.app.representation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Alert(
    show: MutableState<Boolean>,
    message: String?,
    onConfirm: () -> Unit = {}
) {
    if (show.value) {
        AlertDialog(
            onDismissRequest = {
                show.value = false
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message ?: "Erro desconhecido",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.customColorsShema.title,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.customColorsShema.title,
                    modifier = Modifier.clickable {
                        show.value = false
                        onConfirm()
                    }
                )
            },
        )
    }
}