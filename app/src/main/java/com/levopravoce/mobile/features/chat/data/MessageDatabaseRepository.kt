package com.levopravoce.mobile.features.chat.data

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import com.levopravoce.mobile.features.app.data.dto.MessageType
import com.levopravoce.mobile.features.chat.data.entity.Message
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.util.Base64
import java.util.Collections
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageDatabaseRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val context: Context
) {
    fun getMessagesByChannel(
        channelId: Long,
    ): Flow<List<Message>> = messageDao.getMessagesByChannel(channelId);

    fun getMessagesByChannelAndRange(
        channelId: Long,
        date: Long
    ): Flow<List<Message>> = messageDao.getMessagesByChannelAndRange(channelId, date);

    fun getMaxDateByChannel(
        channelId: Long
    ) = messageDao.getMaxDateByChannel(channelId);

    suspend fun saveSocketImageMessage(
        messageSocketDto: MessageSocketDTO
    ) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val filePath: String = Environment.DIRECTORY_PICTURES + "/LevoPraVoce/" + fileName
        val outputStream = context.contentResolver.openOutputStream(
            Uri.parse(filePath)
        )

        outputStream.use {
            val bytes: ByteArray = Base64.getDecoder().decode(messageSocketDto.message)
            it?.write(bytes)
        }

        val message = Message(
            imagePath = filePath,
            channelId = messageSocketDto.channelId,
            date = Date(messageSocketDto.timestamp ?: System.currentTimeMillis()),
            type = MessageType.IMAGE,
            text = null,
            sender = messageSocketDto.sender ?: ""
        )

        messageDao.save(message)
    }

    suspend fun saveSocketMessage(messageSocketDto: MessageSocketDTO) {
        val messageEntity = mountMessageEntity(messageSocketDto)

        messageDao.save(messageEntity)
    }

    suspend fun saveSocketMessages(messages: List<MessageSocketDTO>) {
        messages.map { messageSocketDto ->
            mountMessageEntity(messageSocketDto)
        }.let {
            messageDao.saveAll(it)
        }
    }

    private fun mountMessageEntity(messageSocketDto: MessageSocketDTO): Message {
        return Message(
            channelId = messageSocketDto.channelId,
            date = Date(messageSocketDto.timestamp ?: System.currentTimeMillis()),
            text = messageSocketDto.message,
            type = messageSocketDto.type ?: MessageType.TEXT,
            imagePath = null,
            sender = messageSocketDto.sender ?: ""
        )
    }
}