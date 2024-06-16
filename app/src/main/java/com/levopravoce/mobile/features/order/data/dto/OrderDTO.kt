package com.levopravoce.mobile.features.order.data.dto

import com.levopravoce.mobile.features.auth.data.dto.UserDTO

data class OrderDTO (
    var id: Long? = null,
    var height: Double? = null,
    var width: Double? = null,
    var maxWeight: Double? = null,
    var deliveryDate: String? = null,
    var haveSecurity: Boolean? = null,
    var status: OrderStatus? = null,
    var value: Double? = null,
    var deliveryman: UserDTO? = null,
    var client: UserDTO? = null,
    var averageRating: Double? = null,
    var destinationLatitude: Double? = null,
    var destinationLongitude: Double? = null,
    var originLatitude: Double? = null,
    var originLongitude: Double? = null,
    var destinationAddress: String? = null,
    var originAddress: String? = null,
    var price: Double? = null,
    //    private VehicleDTO vehicle;
    //    private PaymentDTO payment;
)