package com.levopravoce.mobile.features.order.represatation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.domain.OrderViewModel


@Composable
fun DeliverymanListInfo(
    order: OrderDTO = OrderDTO(),
) {
val orderViewModel = hiltSharedViewModel<OrderViewModel>()
    val orderViewModelState = orderViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var orderDTO by remember { mutableStateOf(order) }

    val hideKeyboard = {
        keyboardController?.hide()
    }

    val nextFocus = {
        focusManager.moveFocus(FocusDirection.Next)
    }

    val showError = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = orderViewModel) {
        Log.d("DeliverymanListInfo", "Calling listDeliverymans")
        orderViewModel.listDeliverymans()
        Log.d("DeliverymanListInfo", "Status after calling listDeliverymans: ${orderViewModel.uiState.value.status}")
    }

    val deliveryMan = orderViewModel.uiState.collectAsState().value.users

    Screen(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ){
        Alert(show = showError, message = orderViewModelState.value.error ?: "Erro ao carregar entregadores")
        Column(
            Modifier.padding(vertical = 16.dp)
        ){
            BackButton(
                Modifier.scale(1.5f), orderViewModel.uiState.collectAsState().value.status != RequestStatus.LOADING
            )
        }
        when(orderViewModel.uiState.collectAsState().value.status){
            RequestStatus.LOADING -> {
                Log.d("DeliverymanListInfo", "Loading")
                Text("Loading")
            }
            RequestStatus.SUCCESS -> {
                Log.d("DeliverymanListInfo", "Success")
                if(deliveryMan != null){
                    LazyColumn {
                        items(deliveryMan.size){
                            deliveryMan[it].name?.let { it1 -> Text(text = it1) }
                        }
                    }
                } else{
                    Text(text = "Nenhum entregador disponível")
                }
            }
            RequestStatus.ERROR -> {
                Log.d("DeliverymanListInfo", "Error")
                Text("Erro ao carregar entregadores")
            }
            else -> {}
        }
    }
}
