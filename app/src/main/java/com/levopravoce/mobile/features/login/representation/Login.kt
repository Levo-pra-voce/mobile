package com.levopravoce.mobile.features.login.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Loading
import com.levopravoce.mobile.features.login.domain.LoginViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val navController = navControllerContext.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val hideKeyboard = {
        keyboardController?.hide()
    }

    val nextFocus = {
        focusManager.moveFocus(FocusDirection.Next)
    }

    Column(
        Modifier
            .background(color = MaterialTheme.customColorsShema.background)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 24.dp)
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Column {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_16),
                    contentDescription = "seta para esquerda",
                    modifier = Modifier
                        .height(32.dp)
                        .width(24.dp)
                        .clickable {
                            navController?.popBackStack()
                        },
                    colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.placeholder)
                )
            }

            Column(
                Modifier
                    .padding(top = 128.dp, bottom = 72.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Digite seu e-mail:",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                )
                FormInputText(
                    onChange = { email = it },
                    value = email,
                    placeHolder = "email@*****.com",
                    onSubmitted = nextFocus,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Column(modifier = Modifier.padding(bottom = 24.dp)) {
                Text(
                    text = "Digite sua senha:",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                )
                FormInputText(
                    onChange = { password = it },
                    value = password,
                    placeHolder = "*****",
                    visualTransformation = PasswordVisualTransformation(),
                    onSubmitted = hideKeyboard,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Column {
                Text(
                    text = "Esqueceu sua senha?",
                    color = MaterialTheme.customColorsShema.placeholder,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable {
                            navController?.navigate(Routes.Auth.FORGOT_PASSWORD)
                        }
                        .padding(top = 16.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 36.dp)
                ) {
                    NextButton(
                        loginViewModel = viewModel,
                        email = email,
                        password = password,
                        resetFields = {
                            email = ""
                            password = ""
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun NextButton(
    loginViewModel: LoginViewModel,
    email: String = "",
    password: String = "",
    resetFields: () -> Unit
) {
    val navController = navControllerContext.current
    val uiState = loginViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        InvalidLoginDialog(showDialogState = showDialog)
    }

    Button(
        onClick = {
            coroutineScope.launch {
                val (result) = loginViewModel.loginRequest(email, password)
                if (result != null) {
                    resetFields()
                    navController?.navigate(Routes.Home.ROUTE)
                } else {
                    showDialog.value = true
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.customColorsShema.button,
        ),
        enabled = uiState.value.status != RequestStatus.LOADING
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            if (uiState.value.status != RequestStatus.LOADING) {
                Text(
                    text = "Avançar",
                    fontSize = 16.sp,
                    color = MaterialTheme.customColorsShema.title
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Loading(
                        size = 12.dp,
                    )
                }
            }
        }
    }
}

@Composable
fun InvalidLoginDialog(
    showDialogState: MutableState<Boolean>
) {
    AlertDialog(onDismissRequest = {
        showDialogState.value = false
    }, confirmButton = {
        TextButton(onClick = {
            showDialogState.value = false
        }) {
            Text("Ok")
        }
    }, title = {
        Text(text = "Login inválido")
    }, text = {
        Text(text = "Verifique seu e-mail e senha e tente novamente.")
    })
}