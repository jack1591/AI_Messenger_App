package com.example.aimessengerapp.View.MainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel

//строка поиска по чатам
@Composable
fun SearchChatBar(chatViewModel:ChatViewModel, searchChat: String, onClick:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(start = 15.dp),
            value = searchChat,
            onValueChange = { newText ->
                chatViewModel.onSearchChatChange(newText)
            }
        )
        IconButton(onClick = {
            onClick()
        }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search for message")
        }

    }
}