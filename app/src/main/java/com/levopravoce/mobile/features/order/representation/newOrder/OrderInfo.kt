package com.levopravoce.mobile.features.order.representation.newOrder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.levopravoce.mobile.common.RequestStatus
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.FormInputDate
import com.levopravoce.mobile.features.app.representation.FormInputText
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.dto.OrderStatus
import com.levopravoce.mobile.features.order.domain.OrderViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class OrderInfoState {
    CREATE_MAP_SELECTION, CREATE_ORDER_FIELDS, CREATE_DELIVERYMAN_LIST,
}

@Composable
fun OrderInfo(
    orderViewModel: OrderViewModel = hiltSharedViewModel()
) {
    var orderDTOState by remember { mutableStateOf(OrderDTO()) }
    val state = orderViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val navController = navControllerContext.current
    val mapViewDisplay = remember { mutableStateOf(false) }
    var orderInfoState by remember { mutableStateOf(OrderInfoState.CREATE_ORDER_FIELDS) }

    val hideKeyboard = {
        keyboardController?.hide()
    }

    val nextFocus = {
        focusManager.moveFocus(FocusDirection.Next)
    }

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

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val lastOrderInPending = orderViewModel.getCurrentOrderInPending()
            if (lastOrderInPending != null) {
                if (lastOrderInPending.status == OrderStatus.ESPERANDO) {
                    orderDTOState = lastOrderInPending
                    orderInfoState = OrderInfoState.CREATE_DELIVERYMAN_LIST
                }
            }
        }
    }

    Alert(show = showError, message = state.value.error)
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        when (orderInfoState) {
            OrderInfoState.CREATE_ORDER_FIELDS -> {
                Screen(
                    Modifier.verticalScroll(
                        enabled = !mapViewDisplay.value, state = rememberScrollState()
                    )
                ) {
                    Column(
                        Modifier.padding(vertical = 16.dp)
                    ) {
                        BackButton(
                            Modifier.scale(1.5f), state.value.status != RequestStatus.LOADING
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(top = 48.dp, bottom = 24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                                    orderDTOState = orderDTOState.copy(height = number)
                                }
                            },
                            value = orderDTOState.height?.toString() ?: "",
                            placeHolder = "Largura:",
                            withBorder = false,
                            onSubmitted = nextFocus,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                        FormInputText(
                            enabled = orderDTOState.id == null,
                            onChange = {
                                if (it.toDoubleOrNull() != null) {
                                    val number = it.toDouble()
                                    orderDTOState = orderDTOState.copy(width = number)
                                }
                            },
                            value = orderDTOState.width?.toString() ?: "",
                            placeHolder = "Altura:",
                            withBorder = false,
                            onSubmitted = nextFocus,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                        FormInputText(
                            enabled = orderDTOState.id == null,
                            onChange = {
                                if (it.toDoubleOrNull() != null) {
                                    val number = it.toDouble()
                                    orderDTOState = orderDTOState.copy(maxWeight = number)
                                }
                            },
                            value = orderDTOState.maxWeight?.toString() ?: "",
                            placeHolder = "Peso máximo:",
                            withBorder = false,
                            onSubmitted = nextFocus,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                        FormInputDate(
                            value = orderDTOState.deliveryDate ?: "",
                            placeHolder = "Data de entrega:",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            orderDTOState = orderDTOState.copy(deliveryDate = it)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = "Adicionar seguro:",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.customColorsShema.title
                            )
                            RoundedCheckbox(modifier = Modifier.padding(start = 8.dp, top = 10.dp),
                                checked = orderDTOState.haveSecurity ?: false,
                                onCheckedChange = {
                                    orderDTOState = orderDTOState.copy(haveSecurity = it)
                                })
                        }
                    }
                    EnterButton("Avançar") {
                        coroutineScope.launch {
                            orderInfoState = OrderInfoState.CREATE_MAP_SELECTION
                        }
                    }
                }
            }

            OrderInfoState.CREATE_MAP_SELECTION -> {
                Screen(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                ) {
                    Column(
                        Modifier
                            .background(MaterialTheme.customColorsShema.background)
                            .padding(vertical = 16.dp)
                    ) {
                        BackButton(
                            Modifier.scale(1.5f), state.value.status != RequestStatus.LOADING
                        ) {
                            mapViewDisplay.value = false
                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Selecione o ponto de partida e destino",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.customColorsShema.title,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    val showFinishButton = remember {
                        mutableStateOf(false)
                    }
                    Box {
                        Box(modifier = Modifier.fillMaxSize()) {
                            MapSelectDestination(
                                originLatitude = orderDTOState.originLatitude,
                                originLongitude = orderDTOState.originLongitude,
                                destinationLatitude = orderDTOState.destinationLatitude,
                                destinationLongitude = orderDTOState.destinationLongitude
                            ) { originLng, destinationLng ->
                                orderDTOState.destinationLongitude = destinationLng.longitude
                                orderDTOState.destinationLatitude = destinationLng.latitude
                                orderDTOState.originLongitude = originLng.longitude
                                orderDTOState.originLatitude = originLng.latitude
                                showFinishButton.value = true
                            }
                        }
                        if (showFinishButton.value) {
                            Box(
                                Modifier.zIndex(1f)
                            ) {
                                EnterButton("Avançar") {
                                    coroutineScope.launch {
                                        val isSuccess = orderViewModel.createOrder(orderDTOState)
                                        if (isSuccess) {
                                            orderInfoState = OrderInfoState.CREATE_DELIVERYMAN_LIST
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            OrderInfoState.CREATE_DELIVERYMAN_LIST -> {
                DeliverymanListInfo(
                    order = orderDTOState
                )
            }
        }
    }
}

@Composable
fun RoundedCheckbox(
    checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .clip(RoundedCornerShape(50))
            .background(if (checked) Color.Green else Color.Gray)
            .clickable { onCheckedChange(!checked) }, contentAlignment = Alignment.Center
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
    title: String,
    onSubmit: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.customColorsShema.invertBackground)
            .clip(RoundedCornerShape(20))
            .clickable {
                onSubmit()
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
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