package com.levopravoce.mobile.features.configuration.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun CustomizationConfig() {

    val navController = navControllerContext.current

    Column(Modifier.padding(20.dp)) {
        Text(text = "Opções", style = MaterialTheme.typography.titleMedium, color = Color(0xFF535AFF), modifier = Modifier.padding(bottom = 24.dp))

        ItemOption(
            title = "Customizar",
            description = "Costumize a aparência do seu aplicativo.",
            onClick = {
                navController?.navigate(Routes.Home.THEME_CUSTOMIZATION)
            }
        )
        Row (modifier = Modifier.padding(top = 8.dp)) {
            ItemOption(
                title = "Editar",
                description = "Edite as informações do seu perfíl.",
                onClick = {}
            )
        }
    }
}

@Composable
private fun ItemOption(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.customColorsShema.background)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = title,
            color = MaterialTheme.customColorsShema.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = description,
            color = MaterialTheme.customColorsShema.listTitle,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun CustomizationConfigPreview() {
    MobileTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.customColorsShema.background)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            CustomizationConfig()
        }
    }
}