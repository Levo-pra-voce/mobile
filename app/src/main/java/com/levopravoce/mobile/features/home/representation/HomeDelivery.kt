package com.levopravoce.mobile.features.home.representation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.gson.Gson
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.app.representation.Loading
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.home.data.IconDescriptorData
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.dto.OrderStatus
import com.levopravoce.mobile.features.tracking.data.OrderTrackingDTO
import com.levopravoce.mobile.features.tracking.data.OrderTrackingStatus
import com.levopravoce.mobile.features.tracking.domain.TrackingViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch

private val firstLineDescriptorData = listOf(
    IconDescriptorData(
        id = R.drawable.truck_icon,
        contentDescription = "icone para ver suas entregas",
        title = "Suas entregas",
        imageModifier = Modifier.offset(x = -(18.dp), y = 4.dp),
        route = Routes.Home.DELIVERY_MAN.replace("{screen}", "null")
    ),
    IconDescriptorData(
        id = R.drawable.report_icon,
        contentDescription = "icone para ver relatórios",
        title = "Relatórios",
        imageModifier = Modifier.offset(x = -(12.dp), y = 4.dp),
        route = Routes.Home.RELATORY
    ),
)

@Composable
fun HomeDelivery(
    trackingViewModel: TrackingViewModel = hiltSharedViewModel()
) {
    val messageState = trackingViewModel.webSocketState.collectAsState()
    val currentTrackingState by trackingViewModel.currentTrackingState.collectAsState()
    val firstRenderState = trackingViewModel.firstRender.collectAsState()
    val navController = navControllerContext.current
    LaunchedEffect(Unit) {
        trackingViewModel.connectWebSocket()
        trackingViewModel.setStateCurrentTracking()
        trackingViewModel.setFirstRender()
        println("Delivery: Unit effect")
    }

    LaunchedEffect(currentTrackingState) {
        if (currentTrackingState !is ApiResponse.Loading) {
            trackingViewModel.setFirstRender()
        }
    }

    LaunchedEffect(messageState.value) {
        println("Delivery: messageState.value: ${messageState.value}")
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
        val haveTracking =
            currentTrackingState is ApiResponse.Success<OrderDTO?> && (currentTrackingState as ApiResponse.Success<OrderDTO?>).data != null
        if (!haveTracking) {
            BackHandler {

            }
            Column {
                UserHeader()
                UserOptions()
            }
        } else {
            navController?.navigate(Routes.Home.DELIVERY_TRACKING_DRIVER)
        }
    }
}

@Composable
private fun UserOptions(
    mainViewModel: MainViewModel = hiltSharedViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val thirdLineDescriptorData = listOf(
        IconDescriptorData(
            id = R.drawable.exit_icon,
            contentDescription = "icone para sair do aplicativo",
            title = "Sair",
            imageModifier = Modifier.offset(x = -(10.dp)),
            onClick = {
                coroutineScope.launch {
                    mainViewModel.logout()
                }
            }
        ),
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
        RowOption(
            horizontalArrangement = Arrangement.SpaceBetween,
            iconDescriptorData = firstLineDescriptorData,
            modifier = Modifier
        )
        RowOption(
            horizontalArrangement = Arrangement.SpaceBetween,
            iconDescriptorData = thirdLineDescriptorData,
            modifier = Modifier.padding(top = 108.dp)
        )
    }
}