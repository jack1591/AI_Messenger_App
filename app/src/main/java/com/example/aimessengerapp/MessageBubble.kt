package com.example.aimessengerapp

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageBubble(
    message: String,
    isUser: Boolean
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ){
        if (isUser) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF6650a4), shape = RoundedCornerShape(12.dp))
                    .padding(15.dp)
            ) {
                Text(text = message, fontSize = 16.sp, color = Color.White)
            }
        }
        else {
            Box(
                modifier = Modifier
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                    .padding(15.dp)
            ) {
                Text(text = message, fontSize = 16.sp)
            }
        }
    }
}