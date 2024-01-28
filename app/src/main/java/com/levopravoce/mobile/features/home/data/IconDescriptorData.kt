package com.levopravoce.mobile.features.home.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.Modifier

data class IconDescriptorData(
    @DrawableRes val id: Int,
    val contentDescription: String,
    val title: String,
    val route: String? = null,
    val imageModifier: Modifier = Modifier
)