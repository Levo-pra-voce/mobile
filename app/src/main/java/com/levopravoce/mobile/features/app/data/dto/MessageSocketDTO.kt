package com.levopravoce.mobile.features.app.data.dto


data class MessageSocketDTO(
    val type: MessageType? = null,
    val message: String? = null,
    val sender: String? = null,
    val timestamp: Long? = null,
    val channelId: Long
) {
}