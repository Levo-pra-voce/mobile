package com.levopravoce.mobile.features.user.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Title
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.Vehicle
import com.levopravoce.mobile.features.user.domain.UserUiState


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
                onChange = { vehicle = vehicle.copy(model = it) },
                value = vehicle.model ?: "",
                placeHolder = "Modelo",
                withBorder = false,
                onSubmitted = nextFocus,
                modifier = Modifier.fillMaxWidth()
            )
            FormInputText(
                onChange = { vehicle = vehicle.copy(color = it) },
                value = vehicle.color ?: "",
                placeHolder = "Cor do veículo",
                withBorder = false,
                onSubmitted = nextFocus,
                modifier = Modifier.fillMaxWidth()
            )
            FormInputText(
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
                onChange = { vehicle = vehicle.copy(plate = it.maxLength(7)) },
                value = vehicle.plate ?: "",
                placeHolder = "Placa",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
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
                onChange = { vehicle = vehicle.copy(height = it.toDoubleOrNull()) },
                value = vehicle.height?.toString() ?: "",
                placeHolder = "Altura máxima suportada",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                onChange = { vehicle = vehicle.copy(width = it.toDoubleOrNull()) },
                value = vehicle.width?.toString() ?: "",
                placeHolder = "Largura máxima suportada",
                withBorder = false,
                onSubmitted = nextFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                onChange = { vehicle = vehicle.copy(maxWeight = it.toDoubleOrNull()) },
                value = vehicle.maxWeight?.toString() ?: "",
                placeHolder = "Peso máximo suportado",
                withBorder = false,
                onSubmitted = nextFocus,
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