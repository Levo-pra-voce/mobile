package com.levopravoce.mobile.features.auth.data.dto

data class Vehicle(
    var plate: String? = null,
    var model: String? = null,
    var color: String? = null,
    var manufacturer: String? = null,
    var renavam: String? = null,
    var vehicleType: VehicleType? = null
)