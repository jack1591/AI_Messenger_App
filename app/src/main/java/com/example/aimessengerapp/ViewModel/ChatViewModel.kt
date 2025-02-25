package com.example.aimessengerapp.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ChatViewModel: ViewModel() {
    private val _messages = mutableStateListOf<Pair<String,Boolean>>()
    val messages: List<Pair<String,Boolean>> get() = _messages

    fun addMessage(message: Pair<String,Boolean>) {
        _messages.add(message)
    }
}