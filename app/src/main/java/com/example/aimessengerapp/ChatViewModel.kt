package com.example.aimessengerapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ChatViewModel: ViewModel() {
    private val _messages = mutableStateListOf<Pair<String,Boolean>>()
    public val messages: List<Pair<String,Boolean>> get() = _messages

    fun addMessage(message: Pair<String,Boolean>) {
        _messages.add(message)
    }
}