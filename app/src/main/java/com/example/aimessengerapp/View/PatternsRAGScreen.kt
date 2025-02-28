package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import com.example.aimessengerapp.ViewModel.RAGViewModel

@Composable
fun PatternsRAGScreen(
    ragViewModel: RAGViewModel
){
    val patternTypes = mutableListOf<String>("Person","Location","Goal")

    Box(
        modifier = Modifier
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(patternTypes) { patternType ->
                PatternBubble(patternType, {ragViewModel.choosePatternName(patternType)})
            }
        }
    }

}