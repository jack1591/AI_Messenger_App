package com.example.aimessengerapp.ViewModel.Chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimessengerapp.ChatModel.ChatObject
import com.example.aimessengerapp.ChatModel.ChatRepository
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.ChatNameModel.ChatEntityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

//ViewModel для вывода сообщений в конкретном чате и управления чатами

class ChatViewModel(private var chatRepository: ChatRepository, private val entityRepository: ChatEntityRepository): ViewModel() {

    //private val _isLoadingMessages = MutableStateFlow(false)
    //val isLoadingMessages: StateFlow<Boolean> = _isLoadingMessages

    //список сообщений чата
    private val _messages = mutableStateListOf<Pair<String,Boolean>>()
    val messages: List<Pair<String,Boolean>> get() = _messages

    //получение сообщений по номеру чата
    fun getMessagesById(chatId: Int){
        viewModelScope.launch {
            //_isLoadingMessages.value = true
            chatRepository.getMessages(chatId).collectLatest { chatObjects ->
                _messages.clear()//очистить список
                chatObjects.forEach { message ->
                    //добавить в чат
                    addMessage(Pair(message.content,message.type=="request"))
                }
            }
            //_isLoadingMessages.value = false
        }
    }

    //добавить сообщение в бд
    fun insert(message: ChatObject) {
        viewModelScope.launch {
            chatRepository.insert(message)
            getMessagesById(message.chatId)
        }
    }

    //добавить сообщение в чат
    fun addMessage(message: Pair<String,Boolean>) {
        _messages.add(message)
    }


    //список чатов
    private val _chats = mutableStateListOf<ChatEntity>()
    val chats: List<ChatEntity> = _chats

    //номер текущего чата
    private val _currentChatIndex = MutableStateFlow<Int?>(null) // Храним индекс текущего чата
    val currentChatIndex: StateFlow<Int?> = _currentChatIndex

    //выбор чата - при запуске приложения
    fun determineChatToSelect(){
        viewModelScope.launch {
            val chatList = entityRepository.getAllChats().first().sortedBy { it.indexAt}

            //если у нас нет чатов - создать новый
            if (chatList.isEmpty()){
                val newChat = ChatEntity(name = "Новый чат", indexAt = 0, clicks = 0, isFavorite = false)
                insertChat(newChat)
                _currentChatIndex.value = 0
                return@launch
            }

            //взять последний чат (по индексу)
            var lastChat = chatList.last()

            //список сообщений в последнем чате
            val messagesInChat = chatRepository.getMessages(lastChat.indexAt).first()
            if (messagesInChat.isEmpty()){
                //если пусто - выбираем его
                _currentChatIndex.value = lastChat.indexAt
            }
            else {
                //если в последнем чате есть сообщения - создаем новый
                val newChat = ChatEntity(
                    name = "Новый чат",
                    indexAt = lastChat.indexAt+1,
                    clicks = 0,
                    isFavorite = false
                )
                insertChat(newChat)
                //устанавливаем номер нового чата в качестве значения переменной currentChatIndex
                _currentChatIndex.value = newChat.indexAt
            }
        }
    }


    init {
        load()
    }

    private fun load() {
        getAllChats()
    }


    // номер для сортироки (по дате = индексу, по популярности = число кликов)
    private val _numberOfSort = mutableStateOf(0)
    val numberOfSort = _numberOfSort

    //вывести все чаты по дате
    fun getAllChats(){
        viewModelScope.launch {
            entityRepository.getAllChats().collectLatest { list ->
                _chats.clear()
                list.forEach { chatName ->
                    _chats.add(chatName)
                }
                _numberOfSort.value = 1
            }
        }
    }

    //вывести все чаты по популярности
    fun getAllChatsByPopularity() {
        viewModelScope.launch {
            entityRepository.getAllChatsByPopularity().collectLatest { list ->
                _chats.clear()
                list.forEach { chatName ->
                    _chats.add(chatName)
                }
                _numberOfSort.value = 2
            }
        }
    }

    //добавить чат
    fun insertChat(chatEntity: ChatEntity) {
        viewModelScope.launch {
            entityRepository.insertChat(chatEntity)
        }
    }

    //обновить чат
    fun updateChat(chatEntity: ChatEntity) {
        viewModelScope.launch {
            entityRepository.updateChat(chatEntity)
        }
    }

    //удалить чат
    fun deleteChat(chat: ChatEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("chatDelete", chat.indexAt.toString())
            chatRepository.deleteMessages(chat.indexAt)
            entityRepository.deleteChat(chat)
        }
    }

    //диалоговое окно для изменения названия чата
    private val _dialogText = MutableStateFlow("")
    val dialogText: StateFlow<String> = _dialogText

    // хранит текст в диалоговом окне редактирования чата
    fun updateDialogText(newText: String) {
        _dialogText.value = newText
    }

    //очистить текст
    fun clearDialogText() {
        _dialogText.value = ""
    }


    // переменная для хранения строки поиска сообщений
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange(text: String){
        _searchText.value = text
    }

    //сохранение индекса прокрутки списка сообщений
    private val _savedListIndex = MutableStateFlow(0)
    val savedListIndex = _savedListIndex.asStateFlow()

    fun onSavedListIndexChange(index: Int){
        _savedListIndex.value = index
    }

    // переменная для хранения строки поиска чатов
    private val _searchChat = MutableStateFlow("")
    val searchChat = _searchChat.asStateFlow()

    fun onSearchChatChange(text: String){
        _searchChat.value = text
    }

    // сохранение индекса прокрутки списка чатов
    private val _savedListChatIndex = MutableStateFlow(0)
    val savedListChatIndex = _savedListChatIndex.asStateFlow()

    fun onSavedListChatIndexChange(index: Int){
        _savedListChatIndex.value = index
    }

    // выбор чата по индексу
    fun selectChat(index: Int){
        _currentChatIndex.value = index
    }

    //переменная для выбора списка чатов (избранные или все)
    private var _isFavorite = MutableStateFlow<Boolean>(false) // Храним индекс текущего чата
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun changeList(){
        _isFavorite.value = !_isFavorite.value
    }
}