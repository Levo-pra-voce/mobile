package com.levopravoce.mobile.features.user.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.app.representation.Submit
import com.levopravoce.mobile.features.user.domain.UserViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import kotlinx.coroutines.launch

@Composable
fun ChangePassword(
    userViewModel: UserViewModel = hiltSharedViewModel(),
    mainViewModel: MainViewModel = hiltSharedViewModel()
) {

    val authState = mainViewModel.authUiStateStateFlow.collectAsStateWithLifecycle();
    val userState = userViewModel.uiState.collectAsStateWithLifecycle();
    val isLoading = userState.value.status == RequestStatus.LOADING
    val coroutineScope = rememberCoroutineScope()
    val navController = navControllerContext.current

    val showError = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(userState.value.status) {
        when (userState.value.status) {
            RequestStatus.SUCCESS -> {
                navController?.navigate(Routes.Auth.LOGIN)
            }
            RequestStatus.ERROR -> {
                showError.value = true
            }
            else -> {}
        }
    }

    Alert(show = showError, message = userState.value.error)

    Screen(padding = 24.dp) {
        if (authState.value.data != null && authState.value.data?.token != null) {
            BackButton(
                Modifier.scale(1.5f)
            )
        }
        var password by remember { mutableStateOf("") }
        var repeatPassword by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            FormInputText(
                onChange = { password = it },
                value = password,
                enabled = !isLoading,
                placeHolder = "********",
                label = "Digite sua nova senha:",
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            FormInputText(
                onChange = { repeatPassword = it },
                value = repeatPassword,
                enabled = !isLoading,
                placeHolder = "********",
                label = "Repite a nova senha:",
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Submit(
                onSubmit = {
                    coroutineScope.launch {
                        userViewModel.changePassword(password, repeatPassword)
                    }
                },
                isLoading = isLoading,
                title = "Enviar nova senha"
            )
        }
    }
}