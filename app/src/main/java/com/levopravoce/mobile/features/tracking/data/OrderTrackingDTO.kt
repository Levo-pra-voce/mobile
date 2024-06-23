package com.levopravoce.mobile.features.tracking.data

data class OrderTrackingDTO(
    var orderId: Long? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var status: OrderTrackingStatus? = null,
)

