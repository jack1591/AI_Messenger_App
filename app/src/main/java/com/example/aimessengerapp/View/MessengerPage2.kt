package com.example.aimessengerapp.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimessengerapp.View.RAG_UI.PatternScreen
import com.example.aimessengerapp.View.RAG_UI.PatternsRAGScreen
import com.example.aimessengerapp.ViewModel.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAGViewModel
import com.example.aimessengerapp.api.NetworkResponse
import com.example.aimessengerapp.api.RequestModel

@Composable
fun MessengerPage2(viewModel: MessageViewModel, chatViewModel: ChatViewModel, ragViewModel: RAGViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = { /*TODO*/ },
        drawerState = drawerState
        )
    {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Text") }
                )
            },
            bottomBar = {
                BottomAppBar (
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
                ){
                    BottomBar(viewModel,chatViewModel,ragViewModel)
                }

            }
        ){ padding->
            ScaffoldMain(padding = padding, chatViewModel = chatViewModel, ragViewModel = ragViewModel)
        }
    }

}
