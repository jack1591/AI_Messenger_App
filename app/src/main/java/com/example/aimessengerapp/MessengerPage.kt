package com.example.aimessengerapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.aimessengerapp.api.NetworkResponse
import com.example.aimessengerapp.api.RequestModel

@Composable
fun MessengerPage(viewModel: MessageViewModel, chatViewModel: ChatViewModel){

    var request by remember{
        mutableStateOf("")
    }

    /*
    val messages = remember{
        mutableStateListOf<Pair<String,Boolean>>()
    }
     */
    val messageResult = viewModel.messageResult.observeAsState()

    val listState = rememberLazyListState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.BottomEnd
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.Bottom,
                state = listState
            ) {
                items(chatViewModel.messages) { message ->
                    MessageBubble(message = message.first, message.second)
                }
            }
        }
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

            IconButton(onClick = {
                    if (request.length>0) {
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
    
    LaunchedEffect(chatViewModel.messages.size) {
        if (chatViewModel.messages.size > 0)
        listState.animateScrollToItem(chatViewModel.messages.size-1)
    }
}

