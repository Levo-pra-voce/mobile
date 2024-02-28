package com.levopravoce.mobile.features.chat.representation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.features.app.data.dto.MessageType
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.chat.data.entity.Message
import com.levopravoce.mobile.features.chat.domain.ChatViewModel
import java.sql.Date

val mockMessages: List<Message> = listOf(
    Message(
        id = 1,
        text = "Hello, world!",
        date = Date(2023, 1, 1),
        sender = "dudubr1441@gmail.com",
        channelId = 1,
        imagePath = null,
        type = MessageType.TEXT
    ),
    Message(
        id = 2,
        text = "Hello, world!",
        date = Date(2023, 1, 1),
        sender = "dudubr1441@gmail.com",
        channelId = 1,
        imagePath = null,
        type = MessageType.TEXT

    ),
)


@Composable
fun ChatList(
    channelId: Long,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val isScrolledToBottom = !listState.canScrollForward;
    val messagesFlow = chatViewModel.currentMessagesFlow;
    val messagesState by messagesFlow.collectAsStateWithLifecycle(initialValue = mockMessages)

    LaunchedEffect(isScrolledToBottom) {
        if (isScrolledToBottom) {
//            listState.scrollToItem(messagesState.size - 1)
        }
    }

    LaunchedEffect(channelId) {
        val (messagesSize, maxMessageDate) = chatViewModel.getMessagesSizeAndMaxDateMessageByChannel(
            channelId
        )

        if (messagesSize == 0) {
            val allMessages = chatViewModel.getAllMessagesByChannel(channelId)
            allMessages.forEach {
                chatViewModel.persistMessageInDatabase(it)
            }
        } else if (maxMessageDate != null) {
            chatViewModel.getMessagesByChannelAndDate(channelId, maxMessageDate)
                .forEach {
                    chatViewModel.persistMessageInDatabase(it)
                }
        }

        chatViewModel.mountMessageChannelFlow(channelId)
    }

    LaunchedEffect(true) {
//        listState.scrollToItem(messagesState.size - 1)
    }

    Screen {
        LazyColumn(
            state = listState,
            reverseLayout = true
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 25.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "\uD83C\uDF3F  Plants in Cosmetics",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            itemsIndexed(messagesState) { _, item ->
                Text(
                    text = item.text ?: "dapklçç~dlaaldlk,as,l",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Red
                )
            }
        }
    }
}