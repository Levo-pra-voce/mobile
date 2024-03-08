package com.levopravoce.mobile.features.chat.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levopravoce.mobile.config.Destination
import com.levopravoce.mobile.config.WebSocketClient
import com.levopravoce.mobile.features.app.data.dto.MessageSocketDTO
import com.levopravoce.mobile.features.app.data.dto.MessageType
import com.levopravoce.mobile.features.chat.data.ChatRepository
import com.levopravoce.mobile.features.chat.data.MessageDatabaseRepository
import com.levopravoce.mobile.features.chat.data.entity.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val chatRepository: ChatRepository,
    private val messageDatabaseRepository: MessageDatabaseRepository
) : ViewModel() {
    private val _currentMessages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val currentMessages = _currentMessages.asStateFlow()

    init {
        webSocketClient.callback = {
            viewModelScope.launch {
                persistMessageInDatabase(it)
            }
        }
    }

    suspend fun mountMessageChannelFlow(channelId: Long) {
        messageDatabaseRepository.getMessagesByChannel(channelId)
            .collect {
                _currentMessages.value = it
            }
    }

    fun getMessagesSizeAndMaxDateMessageByChannel(channelId: Long) =
        messageDatabaseRepository.getMaxDateByChannel(channelId)

    suspend fun persistMessageInDatabase(message: MessageSocketDTO) {
        messageDatabaseRepository.saveSocketMessage(message)
    }

    suspend fun getAllMessagesByChannel(channelId: Long): List<MessageSocketDTO> {
        return chatRepository.getMessagesByChannel(channelId)
    }

    suspend fun getMessagesByChannelAndDate(channelId: Long, date: Long) =
        chatRepository.getMessagesByChannelAndDate(channelId, date)

    suspend fun getChatList() = chatRepository.getChatList()
    suspend fun sendMessage(value: String, channelId: Long) = withContext(Dispatchers.IO) {
        val currentTimestamp = System.currentTimeMillis()
        val message = MessageSocketDTO(
            message = value,
            timestamp = currentTimestamp,
            channelId = channelId,
            type = MessageType.TEXT
        )

        webSocketClient.send(Destination.CHAT, message)
    }
}