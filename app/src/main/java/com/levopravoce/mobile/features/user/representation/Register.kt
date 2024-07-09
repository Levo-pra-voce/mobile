package com.levopravoce.mobile.features.user.representation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.auth.data.dto.Vehicle

val mockClient = UserDTO(
    userType = UserType.CLIENTE,
    name = "Luiz",
    zipCode = "88061412",
    cpf = "84243847096",
    email = "dudubr1441@gmail.com",
    password = "Teste1234",
    phone = "48991407989",
    complement = "Casa",
    acceptTerms = true
)

val mockDelivery = UserDTO(
    userType = UserType.ENTREGADOR,
    name = "Gabriel",
    zipCode = "88061412",
    complement = "Casa",
    cpf = "51604819073",
    cnh = "04033397528",
    phone = "48991407989",
    email = "levopravoceoficial@gmail.com",
    password = "Teste1234",
    acceptTerms = true,
    vehicle = Vehicle(
        plate = "ABC1234",
        model = "Fusca",
        color = "Azul",
        manufacturer = "Volkswagen",
        priceBase = 50.0,
        pricePerKm = 10.0,
        height = 1.5,
        width = 1.5,
        maxWeight = 500.0,
        renavam = "123456789",
    )
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
            DeliveryInfo(
                userDTO = mockDelivery
            ) {
                onBack()
            }
        }

        null -> RegisterDecider { registerType.value = it }
    }
}