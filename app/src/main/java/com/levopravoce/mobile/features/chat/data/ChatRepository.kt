package com.levopravoce.mobile.features.chat.data

import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import com.levopravoce.mobile.features.chat.data.dto.ChatUserDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatRepository {

    @GET("/api/chat/messages/{channelId}")
    suspend fun getMessagesByChannel(
        @Path("channelId") channelId: Long,
    ): List<MessageSocketDTO>

    @GET("/api/chat/messages/{channelId}/{timestamp}")
    suspend fun getMessagesByChannelAndDate(@Path("channelId") channelId: Long, @Path("timestamp") timestamp: Long): List<MessageSocketDTO>

    @GET("/api/chat/list")
    suspend fun getChatList(): List<ChatUserDTO>
}