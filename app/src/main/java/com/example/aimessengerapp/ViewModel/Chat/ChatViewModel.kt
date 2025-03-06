package com.example.aimessengerapp.ViewModel.Chat

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimessengerapp.ChatModel.ChatObject
import com.example.aimessengerapp.ChatModel.ChatRepository
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.ChatNameModel.ChatEntityRepository
import com.example.aimessengerapp.RAGRepositories.RAGObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class ChatViewModel(private var chatRepository: ChatRepository, private val entityRepository: ChatEntityRepository): ViewModel() {
    private val _messages = mutableStateListOf<Pair<String,Boolean>>()
    val messages: List<Pair<String,Boolean>> get() = _messages

    fun getMessagesById(chatId: Int){
        viewModelScope.launch {
            chatRepository.getMessages(chatId).collectLatest { chatObjects ->
                _messages.clear()
                chatObjects.forEach { message ->
                    addMessage(Pair(message.content,message.type=="request"))
                }
            }
        }
    }

    fun insert(message: ChatObject) {
        viewModelScope.launch {
            chatRepository.insert(message)
        }
    }

    fun addMessage(message: Pair<String,Boolean>) {
        _messages.add(message)
    }


    private val _chats = mutableStateListOf<ChatEntity>()
    val chats: List<ChatEntity> = _chats

    private val _currentChatIndex = MutableStateFlow<Int?>(null) // Храним индекс текущего чата
    val currentChatIndex: StateFlow<Int?> = _currentChatIndex

    fun determineChatToSelect(){
        viewModelScope.launch {
            val chatList = entityRepository.getAllChats().first().sortedBy { it.indexAt}

            if (chatList.isEmpty()){
                val newChat = ChatEntity(name = "Новый чат 0", indexAt = 0, clicks = 0)
                insertChat(newChat)
                _currentChatIndex.value = 0
                return@launch
            }

            var lastChat = chatList.last()


            Log.i("number of last chat", lastChat.name+" "+lastChat.indexAt.toString())
            val messages1 = chatRepository.getMessages(lastChat.indexAt).first()
            if (messages1.isEmpty()){
                _currentChatIndex.value = lastChat.indexAt
            }
            else {
                val newChat = ChatEntity(
                    name = "Новый чат ${lastChat.indexAt+1}",
                    indexAt = lastChat.indexAt+1,
                    clicks = 0
                )
                insertChat(newChat)
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


    private val _numberOfSort = mutableStateOf(0)
    val numberOfSort = _numberOfSort

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

    fun insertChat(chatEntity: ChatEntity) {
        viewModelScope.launch {
            entityRepository.insertChat(chatEntity)
        }
    }

    fun updateChat(chatEntity: ChatEntity) {
        viewModelScope.launch {
            entityRepository.updateChat(chatEntity)
        }
    }

    fun deleteChat(chat: ChatEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("chatDelete", chat.indexAt.toString())
            chatRepository.deleteMessages(chat.indexAt)
            entityRepository.deleteChat(chat)
        }
    }

    /*
    fun deleteMessagesById(index: Int) {
        viewModelScope.launch {
            chatRepository.deleteMessages(index)
        }
    }
     */

    fun incrementChatClicks(chatId: Int) {
        viewModelScope.launch {
            entityRepository.incrementChatClicks(chatId)
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


    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange(text: String){
        _searchText.value = text
    }

    private val _savedListIndex = MutableStateFlow(0)
    val savedListIndex = _savedListIndex.asStateFlow()

    fun onSavedListIndexChange(index: Int){
        _savedListIndex.value = index
    }


    private val _searchChat = MutableStateFlow("")
    val searchChat = _searchChat.asStateFlow()

    fun onSearchChatChange(text: String){
        _searchChat.value = text
    }

    private val _savedListChatIndex = MutableStateFlow(0)
    val savedListChatIndex = _savedListChatIndex.asStateFlow()

    fun onSavedListChatIndexChange(index: Int){
        _savedListChatIndex.value = index
    }


}