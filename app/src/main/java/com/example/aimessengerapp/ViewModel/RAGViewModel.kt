package com.example.aimessengerapp.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimessengerapp.RAGRepositories.RAGObject
import com.example.aimessengerapp.RAGRepositories.RAGRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RAGViewModel (

    private val repository: RAGRepository
): ViewModel() {

    private val _ragObjects = MutableStateFlow<List<RAGObject>>(emptyList()) // ✅ Используем Flow
    val ragObjects: StateFlow<List<RAGObject>> = _ragObjects

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) { // ✅ Запускаем в фоне
            repository.get().collectLatest { list ->
                _ragObjects.value = list // ✅ Автоматически обновляет UI
            }
        }
    }

    fun insert(ragObject: RAGObject) {
        viewModelScope.launch {
            repository.insert(ragObject)
        }
    }

    fun delete(ragObject: RAGObject) {
        viewModelScope.launch {
            repository.delete(ragObject)
        }
    }

    fun update(ragObject: RAGObject) {
        viewModelScope.launch {
            repository.update(ragObject)
        }
    }

    private var _isRAG = mutableStateOf<Boolean>(false)
    val isRAG = _isRAG

    fun changeRAG() {
        _isRAG.value = !_isRAG.value
    }

    fun changeRAG_byvalue(_value: Boolean) {
        _isRAG.value = _value
    }
}