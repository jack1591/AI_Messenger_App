package com.example.aimessengerapp.ViewModel.Chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.ChatModel.ChatRepository

class ChatViewModelFactory(
    private val repository: ChatRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(repository) as T
    }
}