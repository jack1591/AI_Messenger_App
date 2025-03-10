package com.example.aimessengerapp.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.aimessengerapp.ChatNameModel.ChatEntity

//оболочка для каждого чата
@Composable
fun ChatNameBubble(
    chat: ChatEntity, //чат
    searchChat: String, //искомый текст (если ищем что-то)
    onClick: () -> Unit, //нажатие на редактирование
    onDelete: () -> Unit, //удаление
    onChooseFavorite: () -> Unit //добавление в избранное/ удаления из избранного
){
    //стркоа для подсветки искомого текста
    var annotatedString: AnnotatedString = buildAnnotatedString {  }

    if (searchChat.isNotBlank()){
        val lowerCasename = chat.name.lowercase()
        val lowerCaseSearch = searchChat.lowercase()

        // Собираем AnnotatedString: обычный текст + подсвеченные совпадения
        annotatedString = buildAnnotatedString {
            var startIndex = 0
            while (true) {
                // Ищем следующее вхождение searchChat в name
                val index = lowerCasename.indexOf(lowerCaseSearch, startIndex)
                if (index == -1) {
                    // Больше совпадений нет: добавить "хвост" обычным стилем
                    append(chat.name.substring(startIndex))
                    break
                } else {
                    // Добавляем часть текста до совпадения
                    append(chat.name.substring(startIndex, index))
                    // Подсвечиваем совпадение (желтым фоном и жирным шрифтом)
                    withStyle(
                        style = SpanStyle(
                            background = Color.Yellow,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(chat.name.substring(index, index + searchChat.length))
                    }
                    startIndex = index + searchChat.length
                }
            }
        }
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (searchChat.isBlank())
            Text(text = chat.name)
        else Text(text = annotatedString)
        Row() {
            //кнопка редактирования чата
            IconButton(onClick = {
                onClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit name of chat"
                )
            }
            // кнопка для избранного
            IconButton(onClick = {
                onChooseFavorite()
            }) {
                Icon(imageVector = Icons.Default.Star,
                    tint = if (chat.isFavorite) Color(0xFFB07D2B) else Color.Gray,
                    contentDescription = "add to chosen")
            }
            //кнопка удаления
            IconButton(onClick = {
                onDelete()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete chat"
                )
            }
        }
    }
}