package com.example.aimessengerapp.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldSubcomposeInMeasureFix
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.ViewModel.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAGViewModel
import com.example.aimessengerapp.api.NetworkResponse
import com.example.aimessengerapp.api.RequestModel

@Composable
fun MessengerPage2(viewModel: MessageViewModel, chatViewModel: ChatViewModel, ragViewModel: RAGViewModel) {

    var request by remember{
        mutableStateOf("")
    }

    val messageResult = viewModel.messageResult.observeAsState()

    Scaffold(
        bottomBar = {
            BottomAppBar (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .padding(top = 20.dp),
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
                                ragViewModel.changeRAG()
                            }
                        ) {
                            Text(text = "RAG", fontSize = 10.sp)
                        }

                        IconButton(onClick = {
                            ragViewModel.changeRAG_byvalue(false)
                            if (request.isNotEmpty()) {
                                chatViewModel.addMessage(Pair(request, true))
                                val requestModel = RequestModel(request)
                                viewModel.getData(requestModel)
                                request = ""
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search_location"
                            )
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

        }
    ){ padding->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            PatternScreen(ragViewModel = ragViewModel, type = "Person")
            /*
            if (!ragViewModel.isRAG.value)
                MessagesList(chatViewModel = chatViewModel)
            else PatternsRAGScreen()
             */
        }
    }

}
