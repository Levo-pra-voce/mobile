package com.levopravoce.mobile.features.configuration.representation

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.levopravoce.mobile.R

@Composable
@Preview
fun PersonIcon(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.person_icon),
        modifier = modifier,
        contentDescription = "icone da pessoa",
        contentScale = ContentScale.FillHeight,
    )
}