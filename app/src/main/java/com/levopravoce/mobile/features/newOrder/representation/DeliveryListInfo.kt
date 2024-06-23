package com.levopravoce.mobile.features.newOrder.representation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

@Composable
fun DeliveryListInfo(user: String, userType: String, deliveryList: List<String>) {
    LazyColumn {
        items(deliveryList.size) { index ->
//            DeliveryListInfo(
//                user = user,
//                userType = userType,
//                delivery = deliveryList[index]
//            )
        }
    }
}