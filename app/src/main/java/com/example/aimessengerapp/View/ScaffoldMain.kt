package com.example.aimessengerapp.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.aimessengerapp.View.RAG_UI.PatternScreen
import com.example.aimessengerapp.View.RAG_UI.PatternsRAGScreen
import com.example.aimessengerapp.ViewModel.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAGViewModel

@Composable
fun ScaffoldMain(padding: PaddingValues, chatViewModel: ChatViewModel, ragViewModel: RAGViewModel){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        if (ragViewModel.patternName.value!=""){
            PatternScreen(ragViewModel = ragViewModel, type = ragViewModel.patternName.value)
        }
        else {
            if (!ragViewModel.isRAG.value)
                MessagesList(chatViewModel = chatViewModel)
            else PatternsRAGScreen(ragViewModel)
        }
    }
}