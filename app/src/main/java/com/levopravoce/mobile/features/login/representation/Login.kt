package com.levopravoce.mobile.features.login.representation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.levopravoce.mobile.navControllerContext


@Composable
fun Login() {
    val navController = navControllerContext.current
    Column {
        Button(onClick = {
            navController.navigate("home")
        }) {

            Text(text = "Login")
        }
    }
}