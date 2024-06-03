package com.levopravoce.mobile.features.user.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.input.maxLength
import com.levopravoce.mobile.common.transformation.MaskVisualTransformation
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.LargeDropdownMenu
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.app.representation.Title
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.user.domain.UserViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun DeliveryInfo(
    userDTO: UserDTO = UserDTO(
        userType = UserType.ENTREGADOR
    ),
    userViewModel: UserViewModel = hiltViewModel()
) {
    var userDTORemember by remember { mutableStateOf(userDTO) }
    val userViewModelState = userViewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val navController = navControllerContext.current
    val vehicleDetailsDisplay = remember { mutableStateOf(false) }
    val isEditing = userDTORemember.id == null

    val hideKeyboard = {
        keyboardController?.hide()
    }

    val nextFocus = {
        focusManager.moveFocus(FocusDirection.Next)
    }

    var showErrorAlert by remember { mutableStateOf(false) }
    LaunchedEffect(userViewModelState.value.status) {
        when (userViewModelState.value.status) {
            RequestStatus.ERROR -> {
                hideKeyboard()
                showErrorAlert = true
            }
            RequestStatus.SUCCESS -> {
                hideKeyboard()
                navController?.navigate(Routes.Home.ROUTE)
            }
            else -> {}
        }
    }

    if (showErrorAlert) {
        AlertDialog(
            onDismissRequest = {
                showErrorAlert = false
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userViewModelState.value.error ?: "Erro desconhecido",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.customColorsShema.title
                    )
                }
            },
            confirmButton = {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.customColorsShema.title,
                    modifier = Modifier.clickable {
                        showErrorAlert = false
                    }
                )
            },
        )
    }

    if (vehicleDetailsDisplay.value) {
        Screen {
            VehicleInfo(
                userDTO = userDTORemember,
                userViewModelState = userViewModelState,
                vehicleDetailsDisplay = vehicleDetailsDisplay,
                isEditing = isEditing,
                hideKeyboard = hideKeyboard,
                nextFocus = nextFocus
            )
        }
    } else {
        Screen(
            Modifier.verticalScroll(
                enabled = !vehicleDetailsDisplay.value,
                state = rememberScrollState()
            )
        ) {
            BackButton(
                Modifier.scale(1.5f),
                userViewModelState.value.status != RequestStatus.LOADING
            )

            Title(text = if (isEditing) "Editar Conta" else "Cadastrar Conta")

            Column {
                FormInputText(
                    onChange = {
                        userDTORemember = userDTORemember.copy(name = it)
                    },
                    value = userDTORemember.name ?: "",
                    placeHolder = "Nome",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                FormInputText(
                    onChange = { userDTORemember = userDTORemember.copy(email = it) },
                    value = userDTORemember.email ?: "",
                    placeHolder = "Email",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FormInputText(
                    onChange = {
                        userDTORemember = userDTORemember.copy(cpf = it.maxLength(11))
                    },
                    value = userDTORemember.cpf ?: "",
                    placeHolder = "CPF",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    visualTransformation = MaskVisualTransformation("###.###.###-##"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FormInputText(
                    onChange = { userDTORemember = userDTORemember.copy(cnh = it.maxLength(11)) },
                    value = userDTORemember.cnh ?: "",
                    placeHolder = "CNH",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    visualTransformation = MaskVisualTransformation("###########"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FormInputText(
                    onChange = {
                        userDTORemember = userDTORemember.copy(phone = it.maxLength(11))
                    },
                    value = userDTORemember.phone ?: "",
                    placeHolder = "Telefone",
                    visualTransformation = MaskVisualTransformation("(##) #####-####"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    withBorder = false,
                    onSubmitted = nextFocus,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FormInputText(
                    onChange = {
                        userDTORemember = userDTORemember.copy(zipCode = it.maxLength(9))
                    },
                    value = userDTORemember.zipCode ?: "",
                    placeHolder = "CEP",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    visualTransformation = MaskVisualTransformation("#####-###"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FormInputText(
                    onChange = { userDTORemember = userDTORemember.copy(complement = it) },
                    value = userDTORemember.complement ?: "",
                    placeHolder = "Complemento",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FormInputText(
                    onChange = { userDTORemember = userDTORemember.copy(password = it) },
                    value = userDTORemember.password ?: "",
                    placeHolder = "Senha",
                    withBorder = false,
                    visualTransformation = PasswordVisualTransformation(),
                    onSubmitted = {
                        hideKeyboard()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                var selectedIndex by remember { mutableIntStateOf(-1) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    LargeDropdownMenu(
                        label = "Tamanho",
                        placeHolder = "Tamanho",
                        items = listOf("Pequeno", "Médio", "Grande"),
                        selectedIndex = selectedIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        onItemSelected = { index, _ -> selectedIndex = index },
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(MaterialTheme.customColorsShema.invertBackground)
                        .clickable {
                            vehicleDetailsDisplay.value = true
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = if (isEditing) "Editar Veículo" else "Cadastrar Veículo",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.customColorsShema.title
                        )
                    }
                }
            }

            SubmitButton(userViewModel, userDTORemember, UserType.ENTREGADOR)
        }
    }
}
