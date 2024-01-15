package com.levopravoce.mobile.features.home.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.levopravoce.mobile.ui.theme.customColorsShema
import com.levopravoce.mobile.R

@Composable
fun HomeClient() {
    Column (
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.customColorsShema.background)
    ) {
        Image(
            painter = painterResource(R.drawable.delivery_up),
            contentDescription = "contentDescription",
            colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.border),
        )
    }
}


@Preview
@Composable
fun HomeClientPreview() {
    HomeClient()
}