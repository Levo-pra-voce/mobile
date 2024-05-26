package com.levopravoce.mobile.features.order.data.dto

import com.levopravoce.mobile.features.auth.data.dto.UserDTO

import java.time.LocalDate

data class OrderDTO (
    var id: Long? = null,
    var height: Double? = null,
    var width: Double? = null,
    var maxWeight: Double? = null,
    var deliveryDate: LocalDate? = null,
    var haveSecurity: Boolean? = null,
    var status: OrderStatus? = null,
    var value: Double? = null,
    var deliveryman: UserDTO? = null,
    var client: UserDTO? = null, //    private VehicleDTO vehicle;
    //    private PaymentDTO payment;
)