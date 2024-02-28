package com.levopravoce.mobile.features.chat.data

import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatRepository {

    @GET("/api/chat/messages/{channelId}")
    suspend fun getMessagesByChannel(
        @Path("channelId") channelId: Long,
    ): List<MessageSocketDTO>
}