package com.levopravoce.mobile.features.app.representation

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable

@Composable
fun PopupMessage(
    message: String,
    onDismiss: () -> Unit,
    delay: Long = 3000,
    show: Boolean = false
) {
    AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = { /*TODO*/ }, )
}