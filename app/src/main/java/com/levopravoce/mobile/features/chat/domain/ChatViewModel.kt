package com.levopravoce.mobile.features.chat.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levopravoce.mobile.config.Destination
import com.levopravoce.mobile.config.WebSocketClient
import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import com.levopravoce.mobile.features.chat.data.ChatRepository
import com.levopravoce.mobile.features.chat.data.MessageDatabaseRepository
import com.levopravoce.mobile.features.chat.data.entity.Message
import com.levopravoce.mobile.features.chat.representation.mockMessages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    webSocketClient: WebSocketClient,
    private val chatRepository: ChatRepository,
    private val messageDatabaseRepository: MessageDatabaseRepository
) : ViewModel() {
    var currentMessagesFlow: SharedFlow<List<Message>> = MutableStateFlow(mockMessages);

    init {
        webSocketClient.messagesFlow.onEach { message ->
            if (message != null) {
                messageDatabaseRepository.saveSocketMessage(message)
            }
        }
    }

    fun mountMessageChannelFlow(channelId: Long) {
        messageDatabaseRepository.getMessagesByChannel(channelId)
            .shareIn(viewModelScope, SharingStarted.Eagerly)
            .run {
                currentMessagesFlow = this
            }
    }

    fun getMessagesSizeAndMaxDateMessageByChannel(channelId: Long) =
        messageDatabaseRepository.getMessagesSizeAndMaxDateMessageByChannel(channelId)

    suspend fun persistMessageInDatabase(message: MessageSocketDTO) {
        messageDatabaseRepository.saveSocketMessage(message)
    }

    suspend fun getAllMessagesByChannel(channelId: Long): List<MessageSocketDTO> {
        return chatRepository.getMessagesByChannel(channelId)
    }

    fun getMessagesByChannelAndDate(channelId: Long, date: Long) = messageDatabaseRepository.getMessagesByChannelAndRange(channelId, date)
}