package com.levopravoce.mobile.features.user.representation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.levopravoce.mobile.features.auth.data.dto.UserType

@Composable
fun Register(
    defaultUserType: UserType? = null
) {
    var registerType by remember { mutableStateOf(defaultUserType) }


    when (registerType) {
        UserType.CLIENTE -> ClientInfo()
        UserType.ENTREGADOR -> DeliveryInfo()
        null -> RegisterDecider { registerType = it }
    }
}