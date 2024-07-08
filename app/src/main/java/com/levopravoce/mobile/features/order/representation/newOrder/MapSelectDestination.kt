package com.levopravoce.mobile.features.order.representation.newOrder

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapSelectDestination(
    originLatitude: Double?,
    originLongitude: Double?,
    destinationLatitude: Double?,
    destinationLongitude: Double?,
    onDestinationSelect: (origin: LatLng, destination: LatLng) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val userLocation = LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.TERRAIN,
                minZoomPreference = 11.0F,
                isTrafficEnabled = false,
                isMyLocationEnabled = false
            )
        )
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false))
    }

    val locationPermissions = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    var originPosition by remember { mutableStateOf(
        if (originLatitude != null && originLongitude != null) {
            LatLng(originLatitude, originLongitude)
        } else {
            null
        }
    ) }
    var destinationPosition by remember { mutableStateOf(
        if (destinationLatitude != null && destinationLongitude != null) {
            LatLng(destinationLatitude, destinationLongitude)
        } else {
            null
        }
    )}

    LaunchedEffect(locationPermissions.status) {
        if (locationPermissions.status == PermissionStatus.Granted) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val lastLocation: Location = it
                        val newLat = LatLng(lastLocation.latitude, lastLocation.longitude)
                        cameraPositionState.move(CameraUpdateFactory.newLatLng(newLat))
                    }
                }
            }
        } else {
            locationPermissions.launchPermissionRequest()
        }
    }

    LaunchedEffect(originPosition, destinationPosition) {
        if (originPosition != null && destinationPosition != null) {
            onDestinationSelect(originPosition!!, destinationPosition!!)
        }
    }

    GoogleMap(
        modifier = Modifier.clip(shape = MaterialTheme.shapes.medium),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapClick = { latLng ->
            if (originPosition == null) {
                originPosition = latLng
            } else if (destinationPosition == null) {
                destinationPosition = latLng
            } else {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Você já selecionou o ponto de partida e destino")
                builder.setMessage("Deseja alterar o ponto de partida ou destino?")
                builder.setPositiveButton("Sim") { _dialog, _which ->
                    originPosition = null
                    destinationPosition = null
                }
                builder.setNegativeButton("Não") { dialog, _which ->
                    dialog.dismiss()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        },
    ) {
        originPosition?.let {
            val markerState = MarkerState(position = it)
            Marker(state = markerState)
        }
        destinationPosition?.let {
            val markerState = MarkerState(position = it)
            Marker(state = markerState)
        }
    }

}