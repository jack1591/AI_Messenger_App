package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aimessengerapp.ChatModel.ChatObject
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel
import com.example.aimessengerapp.VoiceToTextParser
import com.example.aimessengerapp.api.NetworkResponse
import com.example.aimessengerapp.api.RequestModel

@Composable
fun BottomBar(viewModel: MessageViewModel, chatViewModel: ChatViewModel, ragViewModel: RAGViewModel, numberOfChat: Int, voiceToTextParser: VoiceToTextParser){
    val messageResult = viewModel.messageResult.observeAsState()
    val state by voiceToTextParser.state.collectAsState()

    /*
    LaunchedEffect(state.spokenText) {
        if (state.spokenText.isNotEmpty())
            viewModel.request += state.spokenText
    }
     */

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
            if (ragViewModel.chosenName.value!=""){
                viewModel.request+= ragViewModel.chosenName.value
                ragViewModel.clearChosenName()
            }
            if (state.spokenText!=""){
                viewModel.request+= state.spokenText
                voiceToTextParser.clearSpokenText()
            }

            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = viewModel.request+state.spokenText,
                onValueChange = {
                    viewModel.request = it
                },
                label = {
                    Text(text = "ask something")
                })
            Button(modifier = Modifier
                .padding(5.dp),
                onClick = {
                    ragViewModel.changeRAG()
                    ragViewModel.clearChat()
                }
            ) {
                Text(text = "RAG", fontSize = 10.sp)
            }

            IconButton(onClick = {
                if (state.isSpeaking){
                    //voiceToTextParser.stopListening()
                    Log.i("voiceee",state.spokenText)
                }
                else {
                    voiceToTextParser.startListening()
                }
            }) {
                if (state.isSpeaking)
                    Icon(imageVector = Icons.Default.Stop, contentDescription = "stopSound")
                else
                    Icon(imageVector = Icons.Default.Mic, contentDescription = "startSound")
            }

            IconButton(onClick = {
                ragViewModel.changeRAG_byvalue(false)
                ragViewModel.clearChat()
                if (viewModel.request.isNotEmpty()) {
                    chatViewModel.insert(ChatObject(content = viewModel.request,type = "request", chatId = numberOfChat))

                    chatViewModel.addMessage(Pair(viewModel.request, true))

                    val requestModel = RequestModel(viewModel.request)
                    viewModel.getData(requestModel)
                    viewModel.request = ""
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "search_location"
                )
            }
        }
        when (val result = messageResult.value){
            is NetworkResponse.Success -> {
                chatViewModel.insert(ChatObject(content = result.data.response.toString(),type = "response", chatId = numberOfChat))
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