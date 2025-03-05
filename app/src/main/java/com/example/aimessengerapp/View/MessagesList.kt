package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun MessagesList(listState: LazyListState, chatViewModel: ChatViewModel){

    val searchText by chatViewModel.searchText.collectAsState()

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
                MessageBubble(message = message.first, searchText = searchText, message.second)
            }
        }
    }
    LaunchedEffect(chatViewModel.messages.size) {
        if (chatViewModel.messages.isNotEmpty())
            listState.animateScrollToItem(chatViewModel.messages.size-1)
    }
}