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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.levopravoce.mobile.R
import com.levopravoce.mobile.common.input.maxLength
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.features.app.domain.MainViewModel
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.chat.data.entity.Message
import com.levopravoce.mobile.features.chat.domain.ApiResult
import com.levopravoce.mobile.features.chat.domain.ChatViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun Chat(
    channelId: Long,
    channelName: String,
    chatViewModel: ChatViewModel = hiltSharedViewModel(),
    mainViewModel: MainViewModel = hiltSharedViewModel()
) {
    val listState = rememberLazyListState()
    val authState by mainViewModel.authUiStateStateFlow.collectAsStateWithLifecycle()
    val messagesFlow = chatViewModel.currentMessages;
    val messagesState by messagesFlow.collectAsStateWithLifecycle(ApiResult.Loading)
    val currentMessageState = remember {
        mutableStateOf("")
    }
    LaunchedEffect(messagesState) {
        when (messagesState) {
            is ApiResult.Success -> {
                listState.scrollToItem((messagesState as ApiResult.Success<List<Message>>).data?.size ?: 0)
            }

            else -> {}
        }
    }

    val lifecycle = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            chatViewModel.retryConnection()
        }
    }

    LaunchedEffect(channelId) {
        withContext(Dispatchers.IO) {
            chatViewModel.getLastMessages(channelId)
        }
    }

    Screen(padding = 0.dp) {
        Column {
            ChatHeader(channelName = channelName)
            when (messagesState) {
                is ApiResult.Loading -> {
                    Text(
                        text = "Carregando mensagens...",
                        color = MaterialTheme.customColorsShema.title,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is ApiResult.Error -> {
                    Text(
                        text = "Erro ao carregar mensagens",
                        color = MaterialTheme.customColorsShema.title,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is ApiResult.Success -> {
                    val messages = (messagesState as ApiResult.Success<List<Message>>).data ?: emptyList()
                    LazyColumn(
                        state = listState,
                        reverseLayout = true,
                        modifier = Modifier.weight(1f)
                    ) {
                        itemsIndexed(messages) { _, item ->
                            Message(message = item, isCurrentUser = authState.data?.email == item.sender)
                        }
                    }
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