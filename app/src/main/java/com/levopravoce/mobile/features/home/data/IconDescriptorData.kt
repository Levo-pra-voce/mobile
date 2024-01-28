package com.levopravoce.mobile.features.home.data

import androidx.annotation.DrawableRes

data class IconDescriptorData(
    @DrawableRes val id: Int,
    val contentDescription: String,
    val title: String,
    val route: String? = null
)