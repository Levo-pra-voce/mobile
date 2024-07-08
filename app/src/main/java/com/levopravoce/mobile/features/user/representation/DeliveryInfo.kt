package com.levopravoce.mobile.features.user.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.app.representation.Title
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.user.domain.UserViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext

@Composable
fun DeliveryInfo(
    userDTO: UserDTO = UserDTO(
        userType = UserType.ENTREGADOR
    ),
    userViewModel: UserViewModel = hiltViewModel(),
    onBack: (() -> Unit)? = null,
) {
    var userDTORemember by remember { mutableStateOf(userDTO) }
    val userViewModelState = userViewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val navController = navControllerContext.current
    val vehicleDetailsDisplay = remember { mutableStateOf(false) }
    val isEditing = userDTORemember.id != null

    val hideKeyboard = {
        keyboardController?.hide()
    }

    val nextFocus = {
        focusManager.moveFocus(FocusDirection.Next)
    }

    val showErrorAlert = remember { mutableStateOf(false) }
    LaunchedEffect(userViewModelState.value.status) {
        when (userViewModelState.value.status) {
            RequestStatus.ERROR -> {
                hideKeyboard()
                showErrorAlert.value = true
            }

            RequestStatus.SUCCESS -> {
                hideKeyboard()
                navController?.navigate(Routes.Home.ROUTE)
            }

            else -> {}
        }
    }

    Alert(show = showErrorAlert, message = userViewModelState.value.error ?: "Erro desconhecido")

    if (vehicleDetailsDisplay.value) {
        Screen(
            Modifier.verticalScroll(
                enabled = true, state = rememberScrollState()
            )
        ) {
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
                enabled = !vehicleDetailsDisplay.value, state = rememberScrollState()
            )
        ) {
            BackButton(
                Modifier.scale(1.5f),
                userViewModelState.value.status != RequestStatus.LOADING,
            ) {
                if (onBack != null) {
                    onBack()
                } else {
                    navController?.popBackStack()
                }
            }

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
                    modifier = Modifier.fillMaxWidth()
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
                if (!isEditing) {
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
                    Terms(
                        initialValue = userDTORemember.acceptTerms ?: false,
                    ) {
                        userDTORemember = userDTORemember.copy(acceptTerms = it)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    text = if (isEditing) "Editar Veículo" else "Cadastrar Veículo", onClick = {
                        vehicleDetailsDisplay.value = true
                    }, modifier = Modifier.width(200.dp)
                )
            }

            SubmitButton(userViewModel, userDTORemember, UserType.ENTREGADOR)
        }
    }
}
