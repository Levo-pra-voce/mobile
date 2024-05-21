package com.levopravoce.mobile.features.forgotPassword.representation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.features.forgotPassword.domain.ForgotPasswordViewModel

enum class ForgotPasswordState {
    MAIL,
    CODE,
    PASSWORD
}

@Composable
fun ForgotPassword(
    model: ForgotPasswordViewModel = hiltViewModel()
) {
    val forgotPasswordEnum = remember {
        mutableStateOf(ForgotPasswordState.MAIL)
    }

    when (forgotPasswordEnum.value) {
        ForgotPasswordState.MAIL -> ForgotPasswordMail(model, forgotPasswordEnum)
        ForgotPasswordState.CODE -> ForgotPasswordCode(model, forgotPasswordEnum)
        ForgotPasswordState.PASSWORD -> ForgotPasswordChangePassword(model, forgotPasswordEnum)
    }
}