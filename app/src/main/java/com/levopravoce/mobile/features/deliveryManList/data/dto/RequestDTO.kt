package com.levopravoce.mobile.features.deliveryManList.data.dto


class RequestDTO {
    var orderId: Long? = null
    var deliveryManId: Long? = null
    var name: String? = null
    var destinationAddress: String? = null
    var originAddress: String? = null
    var distanceKm: Double? = null
    var deliveryDate: String? = null
    var price: Double? = null
    var averageRating: Double? = null
}
