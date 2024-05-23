package com.levopravoce.mobile.features.order.represatation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.domain.OrderViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OrderInfo(
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    var ordeDTORemember by remember { mutableStateOf(OrderDTO()) }
    val orderViewModelState = orderViewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val navController = navControllerContext.current

    val hideKeyboard = {
        keyboardController?.hide()
    }

    val nextFocus = {
        focusManager.moveFocus(FocusDirection.Next)
    }

    var showErrorAlert by remember { mutableStateOf(false) }

    LaunchedEffect(orderViewModelState.value.status) {
        when (orderViewModelState.value.status) {
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = orderViewModelState.value.error ?: "Erro desconhecido",
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

}