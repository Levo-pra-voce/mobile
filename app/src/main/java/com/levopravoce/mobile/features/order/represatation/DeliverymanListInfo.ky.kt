package com.levopravoce.mobile.features.order.represatation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.domain.OrderViewModel


@Composable
fun DeliverymanListInfo(
    user: UserDTO = UserDTO(
        userType = UserType.ENTREGADOR
    ),
    order: OrderDTO = OrderDTO(),
) {
val orderViewModel = hiltSharedViewModel<OrderViewModel>()
    val orderViewModelState = orderViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var userDTO by remember { mutableStateOf(user) }
    var orderDTO by remember { mutableStateOf(order) }

    val hideKeyboard = {
        //keyboardController?.hide()
    }

    val nextFocus = {
        //focusManager.moveFocus(FocusDirection.Next)
    }

    val showErrorAlert = remember { mutableStateOf(false) }
    //LaunchedEffect(userViewModelState.value.status) {
    //    when (userViewModelState.value.status) {
    //        RequestStatus.ERROR -> {
    //            hideKeyboard()
    //        }
    //    }
    //}

}
