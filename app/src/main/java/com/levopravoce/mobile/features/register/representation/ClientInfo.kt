package com.levopravoce.mobile.features.register.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.input.maxLength
import com.levopravoce.mobile.common.transformation.MaskVisualTransformation
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.ui.theme.customColorsShema

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ClientInfo(
    userDTO: UserDTO = UserDTO(
        userType = UserType.CLIENTE
    )
) {
    var userDTORemember by remember { mutableStateOf(userDTO) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val hideKeyboard = {
        keyboardController?.hide()
    }

    val nextFocus = {
        focusManager.moveFocus(FocusDirection.Next)
    }

    Screen {
        Column {
            BackButton(Modifier.scale(1.5f))
        }

        Column(
            modifier = Modifier
                .padding(top = 48.dp, bottom = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cadastrar Conta",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customColorsShema.title
            )
        }

        Column {
            FormInputText(
                onChange = { userDTORemember = userDTORemember.copy(firstName = it) },
                value = userDTORemember.firstName ?: "",
                placeHolder = "Nome",
                withBorder = false,
                onSubmitted = nextFocus,
                modifier = Modifier
                    .fillMaxWidth()
            )
            FormInputText(
                onChange = { userDTORemember = userDTORemember.copy(email = it) },
                value = userDTORemember.cpf ?: "",
                placeHolder = "Email",
                withBorder = false,
                onSubmitted = nextFocus,
                visualTransformation = MaskVisualTransformation("###.###.###-##"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            FormInputText(
                onChange = { userDTORemember = userDTORemember.copy(cpf = it.maxLength(11)) },
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
                onChange = { userDTORemember = userDTORemember.copy(phone = it.maxLength(11)) },
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
        }
        SubmitBotton()
    }
}

@Composable
private fun SubmitBotton(

) {
    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.customColorsShema.invertBackground),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Cadastrar",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.customColorsShema.title
                )
            }
        }
    }
}

@Preview
@Composable
fun ClientRegisterPreview() {
    ClientInfo()
}