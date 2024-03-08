package com.levopravoce.mobile.features.chat.representation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.input.maxLength
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.chat.domain.ChatViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun Chat(
    channelId: Long,
    channelName: String,
    chatViewModel: ChatViewModel = hiltSharedViewModel()
) {
    val listState = rememberLazyListState()
    val messagesFlow = chatViewModel.currentMessages;
    val messagesState by messagesFlow.collectAsStateWithLifecycle(emptyList())
    val currentMessageState = remember {
        mutableStateOf("")
    }
    LaunchedEffect(messagesState) {
        listState.animateScrollToItem(messagesState.size)
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
    Screen(padding = 0.dp) {
        Column {
            ChatHeader(channelName = channelName)
            LazyColumn(
                state = listState,
                reverseLayout = false,
                modifier = Modifier.weight(1f)
            ) {
                items(messagesState.size) { index ->
                    Message(message = messagesState[index])
                }
            }
            ChatBar(
                currentMessageState = currentMessageState,
                chatViewModel = chatViewModel,
                channelId = channelId
            )
        }
    }
}

@Composable
private fun ChatHeader(
    channelName: String
) {
    Header(horizontal = Alignment.Start) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BackButton(
                modifier = Modifier
                    .size(20.dp)
                    .offset(y = (-16).dp)
            )
            Image(
                painter = painterResource(R.drawable.person_icon),
                contentDescription = "icone do $channelName",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = channelName.maxLength(20),
                color = MaterialTheme.customColorsShema.title,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}