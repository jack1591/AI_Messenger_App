package com.example.aimessengerapp.View.MainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.aimessengerapp.View.MessagesList
import com.example.aimessengerapp.View.RAG_UI.ChooseFavoriteScreen
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
        if (ragViewModel.ragChat.value!="")
            PatternScreen(ragViewModel = ragViewModel, type = ragViewModel.patternName.value, typeOfChat = ragViewModel.ragChat.value)
        else {
            if (ragViewModel.patternName.value != "") {
                ChooseFavoriteScreen(
                    onAll = { ragViewModel.chooseAll() },
                    onFavorite = { ragViewModel.chooseFavorite() }
                )
            } else {
                if (!ragViewModel.isRAG.value)
                    MessagesList(listState, chatViewModel = chatViewModel)
                else PatternsRAGScreen(ragViewModel)
            }
        }

    }
}