package com.levopravoce.mobile.features.chat.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.chat.domain.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun Chat(
    channelId: Long,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val isScrolledToBottom = !listState.canScrollForward;
    val messagesFlow = chatViewModel.currentMessages;
    val messagesState by messagesFlow.collectAsStateWithLifecycle(emptyList())
    val currentMessageState = remember {
        mutableStateOf("")
    }

    LaunchedEffect(isScrolledToBottom) {
        if (isScrolledToBottom) {
//            listState.scrollToItem(messagesState.size - 1)
        }
    }

    LaunchedEffect(channelId) {
        withContext(Dispatchers.IO) {
            val maxMessageDate = chatViewModel.getMessagesSizeAndMaxDateMessageByChannel(
                channelId
            )

            if (maxMessageDate == null) {
                val allMessages = chatViewModel.getAllMessagesByChannel(channelId)
                allMessages.forEach {
                    chatViewModel.persistMessageInDatabase(it)
                }
            } else {
                chatViewModel.getMessagesByChannelAndDate(channelId, maxMessageDate)
                    .forEach {
                        chatViewModel.persistMessageInDatabase(it)
                    }
            }

            chatViewModel.mountMessageChannelFlow(channelId)
        }
    }
    Screen(verticalArrangement = Arrangement.Bottom, padding = 0.dp) {
        LazyColumn(
            state = listState,
            reverseLayout = false
        ) {
            items(messagesState.size) { index ->
                Message(message = messagesState[index])
            }
        }
        ChatBar(currentMessageState = currentMessageState)
    }

}