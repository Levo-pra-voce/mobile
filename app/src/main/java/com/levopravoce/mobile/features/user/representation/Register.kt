package com.levopravoce.mobile.features.user.representation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType

val mockClient = UserDTO(
    userType = UserType.CLIENTE,
    name = "Luiz",
    zipCode = "88061412",
    cpf = "79228000023",
    email = "dudubr1441@gmail.com",
    password = "Teste1234",
    phone = "48991407989",
    complement = "Casa",
    acceptTerms = true
)

@Composable
fun Register(
    defaultUserType: UserType? = null
) {
    val registerType = remember { mutableStateOf(defaultUserType) }
    val onBack: () -> Unit = { registerType.value = null }

    when (registerType.value) {
        UserType.CLIENTE -> {
            BackHandler {
                onBack()
            }
            ClientInfo(
                userDTO = mockClient
            ) {
                onBack()
            }
        }
        UserType.ENTREGADOR -> {
            BackHandler {
                onBack()
            }
            DeliveryInfo {
                onBack()
            }
        }
        null -> RegisterDecider { registerType.value = it }
    }
}