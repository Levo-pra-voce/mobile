package com.levopravoce.mobile.features.configuration.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Configuration(
) {
    Column {
        Header {
            Text(
                text = "Configurações",
                color = MaterialTheme.customColorsShema.title,
                fontSize = 32.sp,
            )
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