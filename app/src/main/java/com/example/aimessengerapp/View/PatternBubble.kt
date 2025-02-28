package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PatternBubble(
    type: String,
    onChoose: () -> Unit,
){
    OutlinedCard(
        modifier = Modifier
            .size(100.dp)
            .padding(bottom = 20.dp)
            .clickable {
                onChoose()
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = type)
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

}