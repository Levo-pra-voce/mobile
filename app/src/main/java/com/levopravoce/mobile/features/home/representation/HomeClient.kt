package com.levopravoce.mobile.features.home.representation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.Loading
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.home.data.IconDescriptorData
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.tracking.data.OrderTrackingDTO
import com.levopravoce.mobile.features.tracking.data.OrderTrackingStatus
import com.levopravoce.mobile.features.tracking.domain.TrackingViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch


val requestOrder = IconDescriptorData(
    id = R.drawable.delivery_ok,
    contentDescription = "icone para solicitar entrega",
    title = "Solicitar\n" + "entrega",
    route = Routes.Home.SELECT_ORDER
)

val trackOrder = IconDescriptorData(
    id = R.drawable.delivery_call,
    contentDescription = "icone para acompanhar entrega",
    title = "Acompanhar\n" + "entrega",
    route = Routes.Home.DELIVERY_TRACKING_CLIENT
)

@Composable
fun HomeClient(
    trackingViewModel: TrackingViewModel = hiltSharedViewModel()
) {
    val messageState = trackingViewModel.webSocketState.collectAsState()
    val currentTrackingState by trackingViewModel.currentTrackingState.collectAsState()
    val redirectToTrackingState = trackingViewModel.redirectToTracking.collectAsState()
    val navController = navControllerContext.current
    LaunchedEffect(Unit) {
        trackingViewModel.connectWebSocket()
        if (currentTrackingState !is ApiResponse.Success) {
            trackingViewModel.setStateCurrentTracking()
        }
        println("Client: Unit effect")
    }

    LaunchedEffect(messageState.value) {
        println("Client: messageState.value: ${messageState.value}")
        if (messageState.value?.message != null) {
            val gson = Gson()
            val trackingDTO =
                gson.fromJson(messageState.value?.message, OrderTrackingDTO::class.java)
            if (trackingDTO?.status == OrderTrackingStatus.STARTED) {
                navController?.navigate(Routes.Home.DELIVERY_TRACKING_CLIENT)
            }
        }
    }

    if (currentTrackingState is ApiResponse.Loading) {
        Screen {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.customColorsShema.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Loading()
            }
        }
    } else {
        val haveTracking = currentTrackingState is ApiResponse.Success && (currentTrackingState as ApiResponse.Success<OrderDTO?>).data != null
        if (!redirectToTrackingState.value || !haveTracking) {
            BackHandler {

            }
            Column {
                UserHeader()
                UserOptions(
                    haveTracking = haveTracking
                )
            }
        } else {
            navController?.navigate(Routes.Home.DELIVERY_TRACKING_CLIENT)
        }
    }
}

@Composable
fun RowOption(
    horizontalArrangement: Arrangement.Horizontal,
    iconDescriptorData: List<IconDescriptorData>,
    modifier: Modifier = Modifier
) {
    val navController = navControllerContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
    ) {
        iconDescriptorData.forEach {
            IconDescriptor(
                id = it.id,
                contentDescription = it.contentDescription,
                title = it.title,
                onClick = {
                    if (it.onClick != null) {
                        it.onClick.invoke()
                    } else {
                        it.route?.let { route ->
                            navController?.navigate(route)
                        }
                    }
                },
                imageModifier = it.imageModifier
            )
        }
    }
}

@Composable
private fun UserOptions(
    mainViewModel: MainViewModel = hiltSharedViewModel(), haveTracking: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()

    val thirdLineDescriptorData = listOf(
        IconDescriptorData(id = R.drawable.exit_icon,
            contentDescription = "icone para sair do aplicativo",
            title = "Sair",
            imageModifier = Modifier.offset(x = -(10.dp)),
            onClick = {
                coroutineScope.launch {
                    mainViewModel.logout()
                }
            }),
        IconDescriptorData(
            id = R.drawable.configuration_icon,
            contentDescription = "icone para ver as configurações",
            title = "Configurações",
            route = Routes.Home.CONFIGURATION
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.customColorsShema.background)
            .padding(36.dp)
    ) {
        val navController = navControllerContext.current
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconDescriptor(id = requestOrder.id,
                contentDescription = requestOrder.contentDescription,
                title = requestOrder.title,
                onClick = {
                    requestOrder.route?.let { route ->
                        navController?.navigate(route)
                    }
                })
            val showTrackingError = remember { mutableStateOf(false) }
            Alert(show = showTrackingError, message = "Você não possuí nenhuma entrega em andamento.")
            IconDescriptor(id = trackOrder.id,
                contentDescription = trackOrder.contentDescription,
                title = trackOrder.title,
                onClick = {
                    if (!haveTracking) {
                        showTrackingError.value = true
                        return@IconDescriptor
                    }
                    trackOrder.route?.let { route ->
                        navController?.navigate(route)
                    }
                })
        }
        RowOption(
            horizontalArrangement = Arrangement.SpaceBetween,
            iconDescriptorData = thirdLineDescriptorData,
            modifier = Modifier.padding(top = 108.dp)
        )
    }
}


@Preview
@Composable
fun UserOptionsPreview() {
    UserOptions()
}