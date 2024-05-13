package com.levopravoce.mobile.features.forgotPassword.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.routes.navControllerContext

@Composable
fun ForgotPasswordMail() {
    val navController = navControllerContext.current
    Screen(padding = 24.dp) {
        var email by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            FormInputText(
                onChange = { email = it },
                value = email,
                placeHolder = "email@*****.com",
                label = "Digite seu e-mail:",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}