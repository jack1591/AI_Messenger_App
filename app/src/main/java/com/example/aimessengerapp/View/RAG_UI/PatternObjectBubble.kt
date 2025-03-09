package com.example.aimessengerapp.View.RAG_UI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimessengerapp.RAGRepositories.RAGObject
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel

//объект rag-шаблона

@Composable
fun PatternObjectBubble(
    ragObject: RAGObject,
    onUpdate: () -> Unit,
    onDelete: () -> Unit,
    onInsert: () -> Unit,
    onSelect: () -> Unit
    ){
    var isFavorite by remember{mutableStateOf(ragObject.isFavorite)}

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box (
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = ragObject.name, fontSize = 16.sp)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                //отправить в строку ввода
                IconButton(onClick = { onInsert() }) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "send")
                }
                //изменить
                IconButton(onClick = { onUpdate() }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                }

                //добавить в избранное
                IconButton(onClick = {
                    isFavorite = !isFavorite
                    onSelect()
                }) {
                    Icon(imageVector = Icons.Default.Star,
                        tint = if (ragObject.isFavorite) Color(0xFFB07D2B) else Color.Gray,
                        contentDescription = "add to chosen")
                }

                //удалить
                IconButton(onClick = {
                    onDelete()
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                }
            }
        }
    }
}