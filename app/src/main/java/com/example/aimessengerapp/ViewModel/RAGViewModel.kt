package com.example.aimessengerapp.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RAGViewModel : ViewModel() {
    private var _isRAG = mutableStateOf<Boolean>(false)
    val isRAG = _isRAG

    fun changeRAG() {
        _isRAG.value = ! _isRAG.value
    }

    fun changeRAG_byvalue(_value: Boolean) {
        _isRAG.value = _value
    }
}