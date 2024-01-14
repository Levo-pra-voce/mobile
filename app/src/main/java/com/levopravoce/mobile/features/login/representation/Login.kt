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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Loading
import com.levopravoce.mobile.features.auth.data.dto.JwtResponseDTO
import com.levopravoce.mobile.navControllerContext
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch


@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val navController = navControllerContext.current
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
                            navController.popBackStack()
                        },
                    colorFilter = ColorFilter.tint(MaterialTheme.customColorsShema.placeholder)
                )
            }

            Column(
                Modifier
                    .padding(top = 128.dp, bottom = 72.dp)
                    .fillMaxWidth()
            ) {
                FormInputText(
                    onChange = { email = it },
                    value = email,
                    placeHolder = "email@*****.com",
                    label = "Digite seu e-mail:",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Column {
                FormInputText(
                    onChange = { password = it },
                    value = password,
                    placeHolder = "*****",
                    label = "Digite sua senha:",
                    modifier = Modifier
                        .fillMaxWidth()
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
                        loginViewModel = viewModel
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
    password: String = ""
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
                val result: JwtResponseDTO? = loginViewModel.loginRequest(email, password)
                if (result != null) {
                    navController.navigate(Routes.Home.ROUTE)
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
                    Loading()
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