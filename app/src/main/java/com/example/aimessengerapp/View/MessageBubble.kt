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
    message: String,
    searchText: String,
    isUser: Boolean
){

    var annotatedString: AnnotatedString = buildAnnotatedString {  }
    if (searchText.isNotBlank()){
        val lowerCaseMessage = message.lowercase()
        val lowerCaseSearch = searchText.lowercase()

        annotatedString = buildAnnotatedString {
            var startIndex = 0
            while (true) {
                val index = lowerCaseMessage.indexOf(lowerCaseSearch, startIndex)
                if (index == -1) {
                    append(message.substring(startIndex))
                    break
                } else {
                    append(message.substring(startIndex, index))
                    withStyle(
                        style = SpanStyle(
                            background = Color.Yellow,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(message.substring(index, index + searchText.length))
                    }
                    startIndex = index + searchText.length
                }
            }
        }
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ){
        if (isUser) {
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
        else {
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