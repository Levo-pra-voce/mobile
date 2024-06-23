package com.levopravoce.mobile.features.home.representation

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.data.dto.ApiResponse
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.app.representation.Loading
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.home.data.IconDescriptorData
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.dto.OrderStatus
import com.levopravoce.mobile.features.tracking.domain.TrackingViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch


private val firstLineDescriptorData = listOf(
    IconDescriptorData(
        id = R.drawable.delivery_ok,
        contentDescription = "icone para solicitar entrega",
        title = "Solicitar\n" + "entrega",
        route = Routes.Home.SELECT_ORDER
    ),
    IconDescriptorData(
        id = R.drawable.delivery_call,
        contentDescription = "icone para acompanhar entrega",
        title = "Acompanhar\n" + "entrega",
        route = Routes.Home.DELIVERY_TRACKING_CLIENT
    ),
)

private val secondLineDescriptorData = listOf(
    IconDescriptorData(
        id = R.drawable.delivery_persons_icon,
        contentDescription = "icone para para listagem de motoristas",
        title = "Motoras"
    ),
    IconDescriptorData(
        id = R.drawable.message_icon,
        contentDescription = "icone para ver a lista de mensagens",
        title = "Mensagens",
        route = Routes.Home.CHAT_LIST
    ),
)

@Composable
fun HomeClient(
    trackingViewModel: TrackingViewModel = hiltSharedViewModel()
) {
    val currentTrackingState by trackingViewModel.currentTrackingState.collectAsStateWithLifecycle()
    val firstRenderState = trackingViewModel.firstRender.collectAsState()
    val navController = navControllerContext.current
    LaunchedEffect(Unit) { trackingViewModel.setStateCurrentTracking() }

    LaunchedEffect(currentTrackingState) {
        if (currentTrackingState is ApiResponse.Success || currentTrackingState is ApiResponse.Error) {
            trackingViewModel.setFirstRender()
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
        if (!haveTracking || !firstRenderState.value) {
            Column {
                UserHeader()
                UserOptions()
            }
        } else {
            val orderDTO = (currentTrackingState as ApiResponse.Success<OrderDTO?>).data
            when (orderDTO?.status) {
                OrderStatus.EM_PROGRESSO -> {
                    navController?.navigate(Routes.Home.DELIVERY_TRACKING_CLIENT)
                }
                OrderStatus.ENTREGADO -> {
                    navController?.navigate(Routes.Home.CLIENT_PAYMENT)
                }
                else -> {}
            }
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
    mainViewModel: MainViewModel = hiltSharedViewModel()
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
        RowOption(
            horizontalArrangement = Arrangement.SpaceBetween,
            iconDescriptorData = firstLineDescriptorData,
            modifier = Modifier
        )
        RowOption(
            horizontalArrangement = Arrangement.SpaceBetween,
            iconDescriptorData = secondLineDescriptorData,
            modifier = Modifier.padding(top = 108.dp)
        )
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