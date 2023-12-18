package com.levopravoce.mobile.features.login.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levopravoce.mobile.R
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema


@Composable
fun Login() {
    val navController = navControllerContext.current
    Column(
        Modifier
            .background(color = MaterialTheme.customColorsShema.background)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 24.dp)
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Column {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_16),
                    contentDescription = "seta para esquerda",
                    modifier = Modifier
                        .height(32.dp)
                        .width(24.dp)
                        .clickable {
                            navController.popBackStack()
                        },
                    colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.placeholder)
                )
            }


            Column(
                Modifier
                    .padding(top = 128.dp, bottom = 72.dp)
                    .fillMaxWidth()
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
            Column {
                FormInputText(
                    onChange = { password = it },
                    value = password,
                    placeHolder = "*****",
                    label = "Digite sua senha:",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 36.dp)
                ) {
                    NextButton()
                }
            }
        }
    }
}


@Composable
private fun NextButton() {
    val navController = navControllerContext.current
    Button(
        onClick = {

        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.customColorsShema.button,
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Avançar", fontSize = 20.sp)
        }
    }
}