package com.levopravoce.mobile.features.app.data.dto

import com.levopravoce.mobile.config.Destination


data class MessageSocketDTO(
    val destination: Destination,
    val type: MessageType? = null,
    val message: String? = null,
    val sender: String? = null,
    val timestamp: Long? = null,
    val channelId: Long
) {
}