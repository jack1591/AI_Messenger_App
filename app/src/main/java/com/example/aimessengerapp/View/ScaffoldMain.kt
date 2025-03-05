package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.aimessengerapp.RAGRepositories.RAGObject
import com.example.aimessengerapp.View.RAG_UI.Dialogs.UpdateDialog
import com.example.aimessengerapp.View.RAG_UI.PatternScreen
import com.example.aimessengerapp.View.RAG_UI.PatternsRAGScreen
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel

@Composable
fun ScaffoldMain(listState: LazyListState, padding: PaddingValues, chatViewModel: ChatViewModel, ragViewModel: RAGViewModel){


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding(), top = padding.calculateTopPadding()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        if (ragViewModel.patternName.value!=""){
            PatternScreen(ragViewModel = ragViewModel, type = ragViewModel.patternName.value)
        }
        else {
            if (!ragViewModel.isRAG.value)
                MessagesList(listState, chatViewModel = chatViewModel)
            else PatternsRAGScreen(ragViewModel)
        }
    }

}