package com.example.aimessengerapp.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel

@Composable
fun MessagesList(chatViewModel: ChatViewModel, numberOfChat: Int){
    val listState = rememberLazyListState()
    chatViewModel.getMessagesById(numberOfChat)
    Box(
        modifier = Modifier
            .padding(15.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.Bottom,
            state = listState
        ) {
            items(chatViewModel.messages) { message ->
                MessageBubble(message = message.first, message.second)
            }
        }
    }
    LaunchedEffect(chatViewModel.messages.size) {
        if (chatViewModel.messages.isNotEmpty())
            listState.animateScrollToItem(chatViewModel.messages.size-1)
    }
}