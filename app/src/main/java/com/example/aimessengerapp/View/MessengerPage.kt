package com.example.aimessengerapp.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimessengerapp.ViewModel.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.api.NetworkResponse
import com.example.aimessengerapp.api.RequestModel

@Composable
fun MessengerPage(viewModel: MessageViewModel, chatViewModel: ChatViewModel){

    var request by remember{
        mutableStateOf("")
    }

    val messageResult = viewModel.messageResult.observeAsState()

    //var currentScreen by remember { mutableStateOf("screen1") }
    var isVisibleRAG = remember{
        mutableStateOf(false)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (! isVisibleRAG.value)
            MessagesList(chatViewModel = chatViewModel)
        else
            PatternsRAGScreen()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = request,
                onValueChange = {
                    request = it
                },
                label = {
                    Text(text = "ask something")
                })
            Button(modifier = Modifier
                .padding(5.dp),
                onClick = {
                    isVisibleRAG.value = ! isVisibleRAG.value
                }
            ) {
                Text(text = "RAG", fontSize = 10.sp)
            }
            IconButton(onClick = {
                    if (request.isNotEmpty()) {
                        chatViewModel.addMessage(Pair(request, true))
                        val requestModel = RequestModel(request)
                        viewModel.getData(requestModel)
                        request = ""
                    }
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search_location")
            }
        }

        when (val result = messageResult.value){
            is NetworkResponse.Success -> {
                chatViewModel.addMessage(Pair(result.data.response.toString(),false))
                viewModel.clearResponse()
            }
            is NetworkResponse.Error ->{
                Text(text = result.message)
            }
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Waiting ->{

            }
            null -> {}
        }
    }
}

