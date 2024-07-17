package com.levopravoce.mobile.features.user.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.input.maxLength
import com.levopravoce.mobile.common.transformation.MaskVisualTransformation
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Title
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.Vehicle
import com.levopravoce.mobile.features.user.domain.UserUiState

fun removePriceFormat(price: String): Double? {
    if (price.endsWith(",0")) {
        return price.replace(",0", "").replace(",", ".").toDoubleOrNull()
    }
    return price.replace(",", ".").toDoubleOrNull()
}

fun formatPrice(price: Double?): String {
    if (price == null || price == 0.0) return "";

    return price.toString().replace(".", ",")
}
fun formatPrice(price: Int?): String {
    if (price == null || price == 0) return "";

    return price.toString().replace(".", ",")
}

@Composable
fun VehicleInfo(
    userDTO: UserDTO,
    userViewModelState: State<UserUiState>,
    vehicleDetailsDisplay: MutableState<Boolean>,
    isEditing: Boolean,
    hideKeyboard: () -> Unit?,
    nextFocus: () -> Boolean
) {
    var vehicle: Vehicle by remember {
        mutableStateOf(userDTO.vehicle ?: Vehicle())
    }

    val onSubmit = {
        userDTO.vehicle = vehicle
        vehicleDetailsDisplay.value = false
    }

    Column {
        BackButton(
            Modifier.scale(1.5f), userViewModelState.value.status != RequestStatus.LOADING, onSubmit
        )
        Title(text = if (isEditing) "Editar Veículo" else "Cadastrar Veículo")
        Column {
            FormInputText(
                labelModifier = Modifier.offset(y = (-12).dp),
                label = "Modelo",
                onChange = { vehicle = vehicle.copy(model = it) },
                value = vehicle.model ?: "",
                placeHolder = "Modelo",
                withBorder = false,
                onSubmitted = nextFocus,
                modifier = Modifier.fillMaxWidth()
            )
            FormInputText(
                label = "Cor",
                onChange = { vehicle = vehicle.copy(color = it) },
                value = vehicle.color ?: "",
                placeHolder = "Cor do veículo",
                withBorder = false,
                onSubmitted = nextFocus,
                modifier = Modifier.fillMaxWidth()
            )
            FormInputText(
                label = "Montadora",
                onChange = { vehicle = vehicle.copy(manufacturer = it) },
                value = vehicle.manufacturer ?: "",
                placeHolder = "Montadora",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                label = "Placa",
                onChange = { vehicle = vehicle.copy(plate = it.maxLength(7).uppercase()) },
                value = vehicle.plate?.uppercase() ?: "",
                placeHolder = "Placa",
                withBorder = false,
                visualTransformation = MaskVisualTransformation("####-###"),
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                label = "Renavam",
                onChange = { vehicle = vehicle.copy(renavam = it.maxLength(11)) },
                value = vehicle.renavam ?: "",
                placeHolder = "Renavam",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                label = "Altura máxima suportada em metros",
                onChange = {
                    val height = removePriceFormat(it)
                    vehicle = vehicle.copy(height = height)
                },
                value = formatPrice(vehicle.height),
                placeHolder = "Altura máxima suportada",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                label = "Largura máxima suportado em metros",
                onChange = {
                    val width = removePriceFormat(it)
                    vehicle = vehicle.copy(width = width)
                },
                value = formatPrice(vehicle.width),
                placeHolder = "Comprimento máxima suportada",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                label = "Peso máximo suportado em kg",
                onChange = {
                    val weight = removePriceFormat(it)
                    vehicle = vehicle.copy(maxWeight = weight)
                },
                value = formatPrice(vehicle.maxWeight),
                placeHolder = "Peso máximo suportado",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                label = "Preço base por viagem",
                onChange = {
                    val price = removePriceFormat(it)
                    vehicle = vehicle.copy(priceBase = price)
                },
                value = formatPrice(vehicle.priceBase),
                placeHolder = "Preço base por viagem",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                label = "Preço cobrado por km rodado",
                onChange = {
                    val price = removePriceFormat(it)
                    vehicle = vehicle.copy(pricePerKm = price)
                },
                value = formatPrice(vehicle.pricePerKm),
                placeHolder = "Preço cobrado por quilômetro rodado",
                withBorder = false,
                onSubmitted = hideKeyboard,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(text = if (isEditing) "Salvar" else "Cadastrar", onClick = {
                    hideKeyboard()
                    onSubmit()
                })
            }
        }
    }
}