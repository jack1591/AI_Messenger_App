package com.example.aimessengerapp.ViewModel

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.get().collectLatest { list ->
                _ragObjects.value = list
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


    private val _dialogText = MutableStateFlow("")
    val dialogText: StateFlow<String> = _dialogText

    fun updateDialogText(newText: String) {
        _dialogText.value = newText
    }

    fun clearDialogText() {
        _dialogText.value = ""
    }

    /*
    var dialog_field by rememberSaveable {
        mutableStateOf("")
    }
     */
    //var showDialog by mutableStateOf(false)

    private var _patternName = mutableStateOf<String>("")
    val patternName = _patternName

    fun choosePatternName(type: String) {
        _patternName.value = type
    }


    private var _chosenName = mutableStateOf<String>("")
    val chosenName = _chosenName

    fun chooseNameToInsert(pair: Pair<String,String>) {
        Log.i("patternName", pair.second)
        if (pair.second=="Person")
            _chosenName.value = "Imagine that you are a ${pair.first}. "
        else if (pair.second=="Location")
            _chosenName.value = "Imagine that you are in a ${pair.first}. "
        else
            _chosenName.value = "Tell me about ${pair.first}. "

    }

    fun clearChosenName(){
        _chosenName.value=""
    }

    private var _isRAG = mutableStateOf<Boolean>(false)
    val isRAG = _isRAG

    fun changeRAG() {
        _isRAG.value = !_isRAG.value
        if (_isRAG.value==false)
            choosePatternName("")
    }

    fun changeRAG_byvalue(_value: Boolean) {
        _isRAG.value = _value
        choosePatternName("")
    }
}