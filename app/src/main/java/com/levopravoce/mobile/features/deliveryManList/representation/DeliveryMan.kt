package com.levopravoce.mobile.features.deliveryManList.representation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.levopravoce.mobile.features.deliveryManList.representation.delivery.DeliveryList
import com.levopravoce.mobile.features.deliveryManList.representation.request.RequestList

enum class DeliveryManView {
    DELiVERY, REQUEST
}

@Composable
fun DeliveryMan(
    screen: DeliveryManView? = null,
) {
    var deliveryManViewState: MutableState<DeliveryManView?> = remember { mutableStateOf(screen) }

    when (deliveryManViewState.value) {
        DeliveryManView.DELiVERY -> {
            val onBack: () -> Unit = {
                deliveryManViewState.value = null
            }
            BackHandler(onBack = onBack)
            DeliveryList(onBackButton = onBack)
        }

        DeliveryManView.REQUEST -> {
            val onBack: () -> Unit = {
                deliveryManViewState.value = null
            }
            BackHandler(onBack = onBack)
            RequestList(onBackButton = onBack, deliveryManViewState = deliveryManViewState)
        }

        null -> DeliveryManScreenDecider { deliveryManViewState.value = it }
    }
}