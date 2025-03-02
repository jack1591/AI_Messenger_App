package com.example.aimessengerapp.ViewModel.Chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.ChatModel.ChatRepository
import com.example.aimessengerapp.ChatNameModel.ChatEntityRepository

class ChatViewModelFactory(
    private val repository: ChatRepository,
    private val entityRepository: ChatEntityRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(repository, entityRepository) as T
    }
}