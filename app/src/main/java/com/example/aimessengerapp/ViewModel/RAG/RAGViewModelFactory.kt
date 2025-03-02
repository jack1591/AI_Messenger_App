package com.example.aimessengerapp.ViewModel.RAG

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.RAGRepositories.RAGRepository


class RAGViewModelFactory(
    private val repository: RAGRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RAGViewModel(repository) as T
    }
}