package com.levopravoce.mobile.features.chat.representation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.levopravoce.mobile.features.app.representation.BackButton
import com.levopravoce.mobile.features.app.representation.Header
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.chat.data.dto.ChatUserDTO
import com.levopravoce.mobile.features.chat.domain.ChatViewModel
import com.levopravoce.mobile.ui.theme.customColorsShema

@Composable
fun ChatList(
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    var chatList by remember {
        mutableStateOf(listOf<ChatUserDTO>())
    }

    LaunchedEffect(Unit) {
        chatList = chatViewModel.getChatList()
    }

    Screen(padding = 0.dp) {
        Header {
            Row {
                BackButton(
                    modifier = Modifier
                        .offset(x = (-28).dp, y = (-8).dp)
                        .size(28.dp)
                )
                Text(
                    text = "Mensagens",
                    color = MaterialTheme.customColorsShema.title,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
        }

        Column(Modifier.padding(top = 48.dp, start = 16.dp)) {
            LazyColumn {
                items(chatList.size) { chatUserDTO ->
                    ChatItem(chatList[chatUserDTO])
                }
            }
        }
    }
}