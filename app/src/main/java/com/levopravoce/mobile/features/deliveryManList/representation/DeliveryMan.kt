package com.levopravoce.mobile.features.deliveryManList.representation

import androidx.activity.compose.BackHandler
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
fun DeliveryMan(
    screen: DeliveryManView? = null,
) {
    var deliveryManView: DeliveryManView? by remember { mutableStateOf(screen) }

    when (deliveryManView) {
        DeliveryManView.DELiVERY -> {
            val onBack: () -> Unit = {
                deliveryManView = null
            }
            BackHandler(onBack = onBack)
            DeliveryList(onBackButton = onBack)
        }

        DeliveryManView.REQUEST -> {
            val onBack: () -> Unit = {
                deliveryManView = null
            }
            BackHandler(onBack = onBack)
            RequestList(onBackButton = onBack)
        }

        null -> DeliveryManScreenDecider { deliveryManView = it }
    }
}