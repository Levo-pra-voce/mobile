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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class ApiResult <out T> {
    data class Success<out R>(val data: R?): ApiResult<R>()
    data class Error(val message: String): ApiResult<Nothing>()
    object Loading: ApiResult<Nothing>()
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val chatRepository: ChatRepository,
    private val messageDatabaseRepository: MessageDatabaseRepository
) : ViewModel() {
    private val _currentMessages: MutableStateFlow<ApiResult<List<Message>>> = MutableStateFlow(ApiResult.Loading)
    val currentMessages = _currentMessages.asStateFlow()

    init {
        webSocketClient.callback = {
            viewModelScope.launch {
                persistMessageInDatabase(it)
            }
        }
    }

    suspend fun getChatList() = chatRepository.getChatList()
    suspend fun sendMessage(value: String, channelId: Long) = withContext(Dispatchers.IO) {
        val message = MessageSocketDTO(
            message = value,
            channelId = channelId,
            type = MessageType.TEXT
        )

        webSocketClient.send(Destination.CHAT, message)
    }

    suspend fun getLastMessages(channelId: Long) {
        val maxMessageDate = messageDatabaseRepository.getMaxDateByChannel(channelId)
        val messages: List<MessageSocketDTO> = when (maxMessageDate) {
            null -> chatRepository.getMessagesByChannel(channelId)
            else -> chatRepository.getMessagesByChannelAndDate(channelId, maxMessageDate)
        }

        if (messages.isNotEmpty()) {
            this.persistMessagesInDatabase(messages)
        }

        messageDatabaseRepository.getMessagesByChannel(channelId)
            .collect {
                _currentMessages.value = ApiResult.Success(it)
            }
    }

    suspend fun retryConnection() {
        this.webSocketClient.connect()
        webSocketClient.callback = {
            viewModelScope.launch {
                persistMessageInDatabase(it)
            }
        }
    }

    private suspend fun persistMessageInDatabase(message: MessageSocketDTO) {
        messageDatabaseRepository.saveSocketMessage(message)
    }

    private suspend fun persistMessagesInDatabase(messages: List<MessageSocketDTO>) {
        messageDatabaseRepository.saveSocketMessages(messages)
    }
}