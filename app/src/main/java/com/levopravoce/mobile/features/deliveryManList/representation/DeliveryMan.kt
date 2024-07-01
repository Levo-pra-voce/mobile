package com.levopravoce.mobile.features.deliveryManList.representation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.levopravoce.mobile.features.deliveryManList.representation.delivery.DeliveryList
import com.levopravoce.mobile.features.deliveryManList.representation.request.RequestList

enum class DeliveryManView {
    DELiVERY, REQUEST
}

@Composable
fun DeliveryMan() {
    var deliveryManView: DeliveryManView? by remember { mutableStateOf(null) }

    when (deliveryManView) {
        DeliveryManView.DELiVERY -> DeliveryList() {
            deliveryManView = null
        }
        DeliveryManView.REQUEST -> RequestList() {
            deliveryManView = null
        }
        null -> DeliveryManScreenDecider { deliveryManView = it }
    }
}