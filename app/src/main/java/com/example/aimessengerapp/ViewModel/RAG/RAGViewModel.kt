package com.example.aimessengerapp.ViewModel.RAG

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimessengerapp.RAGRepositories.RAGObject
import com.example.aimessengerapp.RAGRepositories.RAGRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


//ViewModel для работы с rag-шаблонами
class RAGViewModel (
    private val repository: RAGRepository
): ViewModel() {

    //шаблоны rag
    private val _ragObjects = MutableStateFlow<List<RAGObject>>(emptyList())
    val ragObjects: StateFlow<List<RAGObject>> = _ragObjects

    init {
        load()
    }

    // загрузка списка шаблонов
    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.get().collectLatest { list ->
                _ragObjects.value = list
            }
        }
    }

    //вставка шаблона
    fun insert(ragObject: RAGObject) {
        viewModelScope.launch {
            repository.insert(ragObject)
        }
    }

    //удаление шаблона
    fun delete(ragObject: RAGObject) {
        viewModelScope.launch {
            repository.delete(ragObject)
        }
    }

    //изменение шаблона
    fun update(ragObject: RAGObject) {
        viewModelScope.launch {
            repository.update(ragObject)
        }
    }


    //диалоговое окно для изменения названия шаблона
    private val _dialogText = MutableStateFlow("")
    val dialogText: StateFlow<String> = _dialogText

    fun updateDialogText(newText: String) {
        _dialogText.value = newText
    }

    fun clearDialogText() {
        _dialogText.value = ""
    }

    //имя текущего шаблона при вставке
    private var _patternName = mutableStateOf<String>("")
    val patternName = _patternName

    fun choosePatternName(type: String) {
        _patternName.value = type
    }


    //имя выбранного шаблона при отправлении запроса
    private var _chosenName = mutableStateOf<String>("")
    val chosenName = _chosenName

    fun chooseNameToInsert(pair: Pair<String,String>) {
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

    //проверка, находимся ли мы в окне выбора rag-шаблонов
    private var _isRAG = mutableStateOf<Boolean>(false)
    val isRAG = _isRAG


    fun changeRAG() {
        _isRAG.value = !_isRAG.value
        if (_isRAG.value==false)
            choosePatternName("")
    }

    //принудительная установка значения для флага
    fun changeRAG_byvalue(_value: Boolean) {
        _isRAG.value = _value
        choosePatternName("")
    }

    //флаг для выбора типа rag-шаблонов - избранные или все
    private var _ragChat = mutableStateOf<String>("")
    val ragChat = _ragChat

    fun chooseAll(){
        _ragChat.value = "ALL"
    }

    fun chooseFavorite(){
        _ragChat.value = "FAVORITE"
    }

    fun clearChat(){
        _ragChat.value = ""
    }
}