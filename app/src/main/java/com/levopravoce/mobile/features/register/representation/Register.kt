package com.levopravoce.mobile.features.register.representation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.levopravoce.mobile.features.auth.data.dto.UserType

@Composable
fun Register() {
    var registerType by remember { mutableStateOf<UserType?>(null) }

    if (registerType == null) {
        RegisterDecider { registerType = it }
    }
}