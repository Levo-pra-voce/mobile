package com.levopravoce.mobile.features.chat.representation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.chat.domain.ChatViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(
    channelId: Long,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val isScrolledToBottom = !listState.canScrollForward;
    val messagesFlow = chatViewModel.currentMessages;
    val messagesState by messagesFlow.collectAsStateWithLifecycle(emptyList())
    var currentMessage by remember {
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
            reverseLayout = true
        ) {
            items(messagesState.size) { index ->
                Message(message = messagesState[index])
            }
        }

        Row {
            TextField(
                value = currentMessage, onValueChange = {
                    currentMessage = it
                }, colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.customColorsShema.title,
                    containerColor = MaterialTheme.customColorsShema.background,
                ), modifier =
                Modifier
                    .background(MaterialTheme.customColorsShema.button)
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }
    }

}