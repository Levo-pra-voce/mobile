package com.levopravoce.mobile.features.forgotPassword.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.app.representation.Submit
import com.levopravoce.mobile.features.forgotPassword.domain.ForgotPasswordViewModel
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordMail(
    model: ForgotPasswordViewModel,
    forgotPasswordEnum: MutableState<ForgotPasswordState>
) {
    val state = model.uiState.collectAsStateWithLifecycle();
    val isLoading = state.value.status == RequestStatus.LOADING
    val coroutineScope = rememberCoroutineScope()
    val showError = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.value.status) {
        when (state.value.status) {
            RequestStatus.SUCCESS -> {
                forgotPasswordEnum.value = ForgotPasswordState.CODE
                model.resetRequestStatus()
            }
            RequestStatus.ERROR -> {
                showError.value = true
            }
            else -> {}
        }
    }

    Alert(show = showError, message = state.value.error)

    Screen(padding = 24.dp) {
        var email by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            FormInputText(
                onChange = { email = it },
                value = email,
                enabled = !isLoading,
                placeHolder = "email@*****.com",
                label = "Digite seu e-mail:",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Submit(
                onSubmit = {
                    coroutineScope.launch {
                        model.sendEmail(email)
                    }
                },
                isLoading = isLoading,
                title = "Enviar e-mail"
            )
        }
    }
}