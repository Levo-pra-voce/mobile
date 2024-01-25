package com.levopravoce.mobile.features.configuration.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.levopravoce.mobile.R
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.navControllerContext
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun Configuration(
) {

    val navController = navControllerContext.current

    Column {
        Header {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_16),
                    contentDescription = "Voltar",
                    colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.title),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .offset(x = (-28).dp, y = (-8).dp)
                        .clickable {
                            navController.popBackStack()
                        }
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