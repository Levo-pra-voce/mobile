package com.levopravoce.mobile.features.configuration.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Configuration(
) {
    Column {
        Header {
            Row {
                BackButton(
                    modifier = Modifier
                        .offset(x = (-28).dp, y = (-8).dp)
                        .size(28.dp)
                )
                Text(
                    text = "Configurações",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = MaterialTheme.customColorsShema.background)
        )
        {
            UserInfo()
            AddressConfig()
            CustomizationConfig()
        }
    }
}

@Composable
@Preview
fun ConfigurationPreview() {
    MobileTheme(
        darkTheme = true
    ) {
        Configuration()
    }
}