package com.levopravoce.mobile.features.tracking.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.bitmapDescriptorFromVector
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.configuration.representation.PersonIcon
import com.levopravoce.mobile.features.order.data.dto.OrderStatus
import com.levopravoce.mobile.features.tracking.data.OrderTrackingDTO
import com.levopravoce.mobile.features.tracking.data.OrderTrackingStatus
import com.levopravoce.mobile.features.tracking.domain.TrackingViewModel
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun ClientTracking(
    trackingViewModel: TrackingViewModel = hiltSharedViewModel()
) {
    val messageState = trackingViewModel.webSocketState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 16f)
    }
    var deliveryUserPosition: LatLng? by remember {
        mutableStateOf(null)
    }
    val orderState by trackingViewModel.orderState.collectAsState()
    val navController = navControllerContext.current
    LaunchedEffect(orderState?.status) {
        if (orderState?.status == OrderStatus.ENTREGADO) {
            navController?.navigate(Routes.Home.CLIENT_PAYMENT) {
                popUpTo(Routes.Home.DELIVERY_TRACKING_CLIENT) {
                    inclusive = true
                }
            }
        }
    }
    val context = LocalContext.current
    LaunchedEffect(messageState.value) {
        if (messageState.value?.message != null) {
            val gson = Gson()
            val trackingDTO =
                gson.fromJson(messageState.value?.message, OrderTrackingDTO::class.java)
            if (trackingDTO.status == OrderTrackingStatus.FINISHED) {
                navController?.navigate(Routes.Home.CLIENT_PAYMENT) {
                    popUpTo(Routes.Home.DELIVERY_TRACKING_CLIENT) {
                        inclusive = true
                    }
                }
            }

            val location = LatLng(trackingDTO?.latitude ?: 0.0, trackingDTO?.longitude ?: 0.0)
            deliveryUserPosition = location
            cameraPositionState.move(CameraUpdateFactory.newLatLng(location))
        }
    }

    LaunchedEffect(Unit) {
        trackingViewModel.connectWebSocket()
        val orderInTracking = trackingViewModel.getCurrentOrderInProgress()
        if (orderInTracking != null) {
            val latLng = LatLng(
                orderInTracking.originLatitude ?: 0.0, orderInTracking.originLongitude ?: 0.0
            )
            cameraPositionState.move(CameraUpdateFactory.newLatLng(latLng))
        } else {
            navController?.popBackStack()
        }
    }

    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.TERRAIN,
                minZoomPreference = 11.0F,
                isTrafficEnabled = false,
                isMyLocationEnabled = false,
            )
        )
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false))
    }
    Screen(padding = 0.dp) {
        Header(
            horizontal = Alignment.CenterHorizontally,
        ) {
            Row {
//                BackButton(
//                    modifier = Modifier
//                        .size(24.dp)
//                        .offset(x = -(32.dp))
//                )
                Text(
                    text = "Entrega em andamento",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
        Column {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
                modifier = Modifier.fillMaxHeight(0.8f)
            ) {
                if (orderState != null) {
                    val homeIcon = bitmapDescriptorFromVector(context, R.drawable.home_map_icon)
                    val originState = MarkerState(
                        position = LatLng(
                            orderState?.originLatitude ?: 0.0, orderState?.originLongitude ?: 0.0
                        )
                    )
                    Marker(
                        state = originState,
                        title = "Origem",
                        snippet = orderState?.originAddress ?: "",
                        icon = homeIcon
                    )

                    val destinationState = MarkerState(
                        position = LatLng(
                            orderState?.destinationLatitude ?: 0.0,
                            orderState?.destinationLongitude ?: 0.0
                        )
                    )
                    Marker(
                        state = destinationState,
                        title = "Destino",
                        snippet = orderState?.destinationAddress ?: "",
                        icon = homeIcon
                    )
                }
                if (deliveryUserPosition != null) {
                    val carIcon = bitmapDescriptorFromVector(context, R.drawable.car_map_icon)
                    val deliveryUserState = MarkerState(position = deliveryUserPosition!!)
                    Marker(
                        state = deliveryUserState,
                        title = "Entregador",
                        snippet = "Entregador",
                        icon = carIcon
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    Modifier.padding(
                        start = 8.dp, bottom = 12.dp
                    )
                ) {
                    Text(
                        text = "Motorista em rota",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.customColorsShema.title
                    )
                }
                Row {
                    PersonIcon()
                    Column(Modifier.padding(start = 12.dp)) {
                        Row {
                            Text(
                                text = orderState?.deliveryman?.name ?: "não informado",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.customColorsShema.title,
                            )
                            Spacer(modifier = Modifier.padding(start = 48.dp))
                            Text(
                                text = orderState?.averageRating?.toString()?.replace(".", ",") ?: "0.0",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.customColorsShema.title
                            )
                        }
                        Row {
                            Text(
                                text = "Placa: ${
                                    orderState?.carPlate ?: "não informada"
                                }",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.customColorsShema.placeholder
                            )
                        }
                    }
                }
            }
        }
    }
}