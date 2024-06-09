package com.levopravoce.mobile.features.order.represatation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.rememberMapViewWithLifecycle
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.map.MapActivity
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.domain.OrderViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OrderInfo(
    model: OrderViewModel = hiltSharedViewModel()
) {
    var orderDTORemember by remember { mutableStateOf(OrderDTO()) }
    val state = model.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val navController = navControllerContext.current
    val mapViewDisplay = remember { mutableStateOf(false) }

    val hideKeyboard = {
        keyboardController?.hide()
    }

    val nextFocus = {
        focusManager.moveFocus(FocusDirection.Next)
    }

    var showErrorAlert by remember { mutableStateOf(false) }

    val showError = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.value.status) {
        when (state.value.status) {
            RequestStatus.SUCCESS -> {
                hideKeyboard()
                navController?.navigate(Routes.Home.ROUTE)
            }
            RequestStatus.ERROR -> {
                hideKeyboard()
                showError.value = true
            }
            else -> {}
        }
    }

    Alert(show = showError, message = state.value.error)

    if(mapViewDisplay.value){
        Screen (
            Modifier.verticalScroll(
                enabled = true,
                state = rememberScrollState()
            )
        ){
            rememberMapViewWithLifecycle(
                orderDTO = orderDTORemember,
                orderViewModelState = state,
                mapDetailsDisplay = mapViewDisplay,
                isEditing = orderDTORemember.id != null,
                hideKeyboard = hideKeyboard,
                nextFocus = nextFocus
            )
        }
    } else{
        Screen(
            Modifier.verticalScroll(
                enabled = !mapViewDisplay.value,
                state = rememberScrollState()
            )
        ) {
            Column {
                BackButton(
                    Modifier.scale(1.5f),
                    state.value.status != RequestStatus.LOADING
                )
            }

            Column (
                modifier = Modifier
                    .padding(top = 48.dp, bottom = 24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Solicitar entrega",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.customColorsShema.title
                )
            }

            Column {
                FormInputText(
                    onChange = {
                        if (it.toDoubleOrNull() != null) {
                            val number = it.toDouble()
                            orderDTORemember = orderDTORemember.copy(height = number)
                        }
                    },
                    value = orderDTORemember.height?.toString() ?: "",
                    placeHolder = "Largura:",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                FormInputText(
                    enabled = orderDTORemember.id == null,
                    onChange = {
                        if (it.toDoubleOrNull() != null) {
                            val number = it.toDouble()
                            orderDTORemember = orderDTORemember.copy(width = number)
                        }
                    },
                    value = orderDTORemember.width?.toString() ?: "",
                    placeHolder = "Altura:",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FormInputText(
                    enabled = orderDTORemember.id == null,
                    onChange = {
                        if (it.toDoubleOrNull() != null) {
                            val number = it.toDouble()
                            orderDTORemember = orderDTORemember.copy(maxWeight = number)
                        }
                    },
                    value = orderDTORemember.maxWeight?.toString() ?: "",
                    placeHolder = "Peso máximo:",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FormInputText(
                    enabled = orderDTORemember.id == null,
                    onChange = {
                        orderDTORemember = orderDTORemember.copy(deliveryDate = it)
                    },
                    value = orderDTORemember.deliveryDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "",
                    placeHolder = "Data de entrega:",
                    withBorder = false,
                    onSubmitted = nextFocus,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        text = "Adicionar seguro:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.customColorsShema.title
                    )
                    RoundedCheckbox(
                        modifier = Modifier.padding(start = 8.dp, top = 10.dp),
                        checked = orderDTORemember.haveSecurity ?: false,
                        onCheckedChange = {
                            orderDTORemember = orderDTORemember.copy(haveSecurity = it)
                        }
                    )
                }
            }
            EnterButton(model, orderDTORemember)
        }
    }
}

@Composable
fun RoundedCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .clip(RoundedCornerShape(50))
            .background(if (checked) Color.Green else Color.Gray)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun EnterButton(
    orderViewModel: OrderViewModel,
    orderDTO: OrderDTO = OrderDTO(),
){
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.customColorsShema.invertBackground)
                .clickable {
                    if (!orderViewModel.isLoading()) {
                        coroutineScope.launch {
                            orderViewModel.createOrder(orderDTO)
                        }
                    }
                }
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
            ){
                Text(
                    text = "Avançar",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.customColorsShema.title
                )
            }
        }
    }
}

@Preview
@Composable
fun OrderInfoPreview() {
    val show = remember {
        mutableStateOf(true)
    }
    Alert(show = show, message = "test")
}