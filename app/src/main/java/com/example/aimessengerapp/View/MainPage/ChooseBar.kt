package com.example.aimessengerapp.View.MainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel

//выбор избранного и сортировки
@Composable
fun ChooseBar(chatViewModel: ChatViewModel, isFavorite: Boolean){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isFavorite)
            Text(text = "Все чаты")
        else
            Text(text = "Избранные чаты")

        IconButton(onClick = {
            //меняем лист при нажатии - избранные или все
            chatViewModel.changeList()
        }) {
            Icon(imageVector = Icons.Default.Star,
                tint = if (isFavorite) Color(0xFFB07D2B) else Color.Gray,
                contentDescription = "add to chosen")
        }

    }

    Spacer(modifier = Modifier.height(20.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (chatViewModel.numberOfSort.value == 1)
            Text(text = "Сортировка по дате")
        else if (chatViewModel.numberOfSort.value == 2)
            Text(text = "Сортировка по популярности")

        Row() {
            IconButton(onClick = {
                //вывод чатов по дате - индексу
                chatViewModel.getAllChats()
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "sort by date"
                )
            }

            IconButton(onClick = {
                //вывод чатов по популярности - числу кликов
                chatViewModel.getAllChatsByPopularity()
            }) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "sort by popularity"
                )
            }
        }
    }

}