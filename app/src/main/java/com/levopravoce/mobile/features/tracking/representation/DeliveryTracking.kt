package com.levopravoce.mobile.features.tracking.representation

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
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
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Loading
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.order.data.dto.OrderStatus
import com.levopravoce.mobile.features.tracking.data.OrderTrackingDTO
import com.levopravoce.mobile.features.tracking.data.OrderTrackingStatus
import com.levopravoce.mobile.features.tracking.domain.TrackingViewModel
import com.levopravoce.mobile.features.tracking.domain.ViewState
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DeliveryTracking(
    trackingViewModel: TrackingViewModel = hiltSharedViewModel()
) {
    val viewState by trackingViewModel.viewState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val locationPermissions = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val context = LocalContext.current
    val navController = navControllerContext.current
    LaunchedEffect(locationPermissions.status) {
        trackingViewModel.handlePermissionEvent(locationPermissions.status)
    }

    when (viewState) {
        is ViewState.Loading -> {
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
        }

        is ViewState.Success -> {
            val userLocation = (viewState as ViewState.Success).location ?: LatLng(0.0, 0.0)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userLocation, 15f)
            }
            val messageState = trackingViewModel.webSocketState.collectAsState()
            var showFinishButton by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(messageState.value) {
                if (messageState.value?.message != null) {
                    val gson = Gson()
                    val trackingDTO =
                        gson.fromJson(messageState.value?.message, OrderTrackingDTO::class.java)
                    if (trackingDTO?.status == OrderTrackingStatus.ITS_CLOSE) {
                        showFinishButton = true
                    }
                }
            }
            val orderState by trackingViewModel.orderState.collectAsState()
            LaunchedEffect(Unit) {
                trackingViewModel.connectWebSocket()
                val orderInTracking = trackingViewModel.getCurrentOrderInProgress()
                if (orderInTracking != null) {
                    val latLng = LatLng(
                        orderInTracking.originLatitude ?: 0.0,
                        orderInTracking.originLongitude ?: 0.0
                    )
                    cameraPositionState.move(CameraUpdateFactory.newLatLng(latLng))
                }
            }
            LaunchedEffect(orderState?.status) {
                if (orderState?.status == OrderStatus.ENTREGADO) {
                    navController?.navigate(Routes.Home.DELIVERY_PAYMENT) {
                        popUpTo(Routes.Home.DELIVERY_TRACKING_DRIVER) {
                            inclusive = true
                        }
                    }
                }
            }
            val properties by remember {
                mutableStateOf(
                    MapProperties(
                        mapType = MapType.TERRAIN,
                        minZoomPreference = 11.0F,
                        isTrafficEnabled = false,
                        isMyLocationEnabled = true
                    )
                )
            }
            val uiSettings by remember {
                mutableStateOf(MapUiSettings(zoomControlsEnabled = false))
            }

            LaunchedEffect(userLocation, orderState) {
                if (orderState != null) {
                    val trackingDTO = OrderTrackingDTO(
                        latitude = userLocation.latitude,
                        longitude = userLocation.longitude,
                        orderId = orderState?.id
                    )
                    trackingViewModel.sendMessage(trackingDTO)
                }
            }

            Screen(
                padding = 0.dp, modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Header(
                    horizontal = Alignment.CenterHorizontally,
                ) {
                    Row {
//                        BackButton(
//                            modifier = Modifier
//                                .size(24.dp)
//                                .offset(x = -(32.dp))
//                        )
                        Text(
                            text = "Local de entrega",
                            color = MaterialTheme.customColorsShema.title,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
                Box {
                    Box(modifier = Modifier.fillMaxSize()) {
                        GoogleMap(
                            cameraPositionState = cameraPositionState,
                            properties = properties,
                            uiSettings = uiSettings
                        ) {
                            if (orderState != null) {
                                val homeIcon =
                                    bitmapDescriptorFromVector(context, R.drawable.home_map_icon)
                                val originState = MarkerState(
                                    position = LatLng(
                                        orderState?.originLatitude ?: 0.0,
                                        orderState?.originLongitude ?: 0.0
                                    )
                                )
                                Marker(
                                    state = originState,
                                    title = "Origem",
                                    snippet = orderState?.originAddress ?: "",
                                    icon = homeIcon,
                                    contentDescription = "Origem"
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
                                    icon = homeIcon,
                                    contentDescription = "Destino"
                                )
                            }
                        }
                    }
                    if (showFinishButton) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 12.dp)
                                .zIndex(1f),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Button("Finalizar") {
                                coroutineScope.launch {
                                    trackingViewModel.finishOrder()
                                    navController?.navigate(Routes.Home.DELIVERY_PAYMENT) {
                                        popUpTo(Routes.Home.DELIVERY_TRACKING_DRIVER) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        is ViewState.RevokedPermissions -> {
            Screen {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.customColorsShema.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(text = "Permitir localização") {
                        locationPermissions.launchPermissionRequest()
                    }
                }
            }
        }
    }
}