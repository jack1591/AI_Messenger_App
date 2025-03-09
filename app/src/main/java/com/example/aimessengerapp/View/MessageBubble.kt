package com.example.aimessengerapp.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageBubble(
    message: String, //содержимое
    searchText: String, //искомые текст (если ищем что-то)
    isUser: Boolean //тип сообщения (запрос или ответ)
){

    //строка для подсветки совпадений текущего сообщения с searchText
    var annotatedString: AnnotatedString = buildAnnotatedString {  }

    if (searchText.isNotBlank()){
        val lowerCaseMessage = message.lowercase() //сообщения (без регистров)
        val lowerCaseSearch = searchText.lowercase() //искомый текст (без регистров)

        annotatedString = buildAnnotatedString {
            var startIndex = 0
            while (true) {
                val index = lowerCaseMessage.indexOf(lowerCaseSearch, startIndex) // находим первое вхождение
                if (index == -1) {
                    //если нет, то обычная строка
                    append(message.substring(startIndex))
                    break
                } else { //иначе
                    //до вхождения - обычный текст
                    append(message.substring(startIndex, index))
                    withStyle(
                        // совпадающая часть - подсветка
                        style = SpanStyle(
                            background = Color.Yellow,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(message.substring(index, index + searchText.length))
                    }
                    //идем дальше
                    startIndex = index + searchText.length
                }
            }
        }
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        //если пользователь - сообщения справа, иначе - слева
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ){
        if (isUser) {  // у запроса - белый текст на фиолетовом фоне
            Box(
                modifier = Modifier
                    .background(Color(0xFF6650a4), shape = RoundedCornerShape(12.dp))
                    .padding(15.dp)
            ) {
                if (searchText.isBlank())
                    Text(text = message, fontSize = 16.sp, color = Color.White)
                else Text(text = annotatedString, fontSize = 16.sp)
            }
        }
        else { // у ответа - черный текст на сером фоне
            Box(
                modifier = Modifier
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                    .padding(15.dp)
            ) {
                if (searchText.isBlank())
                    Text(text = message, fontSize = 16.sp)
                else Text(text = annotatedString, fontSize = 16.sp)
            }
        }
    }
}