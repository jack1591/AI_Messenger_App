package com.example.aimessengerapp.View.MainPage

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel

//строка поиска по сообщениям
@Composable
fun SearchMessageBar(chatViewModel: ChatViewModel, searchText: String, onClick:()->Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(2f),
            value = searchText,
            onValueChange = { newText ->
                chatViewModel.onSearchTextChange(newText)
            }
        )
        IconButton(onClick = {
            onClick()
        }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search for message")
        }
    }
}