package com.levopravoce.mobile.features.user.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun RegisterDecider(
    onClick: (UserType) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.customColorsShema.background)
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Column {
                BackButton(
                    modifier = Modifier.scale(1.5f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Você é ?",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.customColorsShema.title
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ClientOption(
                    value = UserType.CLIENTE, title = "Sou cliente !", onClick = onClick
                )
                Column(Modifier.padding(top = 48.dp)) {
                    ClientOption(
                        value = UserType.ENTREGADOR, title = "Sou entregador !", onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ClientOption(
    value: UserType, title: String, onClick: (UserType) -> Unit = {}
) {
    Button(
        text = title,
        onClick = { onClick(value) },
        modifier = Modifier
            .width(300.dp)
    )
}

@Composable
@Preview
fun RegisterDeciderPreview() {
    ClientOption(
        value = UserType.CLIENTE, title = "Sou cliente !"
    )
}