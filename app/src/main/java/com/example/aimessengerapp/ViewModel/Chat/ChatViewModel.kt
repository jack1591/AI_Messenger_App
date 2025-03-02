package com.example.aimessengerapp.ViewModel.Chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimessengerapp.ChatModel.ChatObject
import com.example.aimessengerapp.ChatModel.ChatRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class ChatViewModel(private var chatRepository: ChatRepository): ViewModel() {
    private val _messages = mutableStateListOf<Pair<String,Boolean>>()
    val messages: List<Pair<String,Boolean>> get() = _messages



    fun getMessagesById(chatId: Int){
        viewModelScope.launch {
            chatRepository.getMessages(chatId).collectLatest { chatObjects ->
                _messages.clear()
                chatObjects.forEach { message ->
                    addMessage(Pair(message.content,message.type=="request"))
                }
            }
        }
    }

    fun insert(message: ChatObject) {
        viewModelScope.launch {
            chatRepository.insert(message)
        }
    }

    fun addMessage(message: Pair<String,Boolean>) {
        _messages.add(message)
    }
}