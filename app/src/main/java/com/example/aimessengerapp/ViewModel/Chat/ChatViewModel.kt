package com.example.aimessengerapp.ViewModel.Chat

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimessengerapp.ChatModel.ChatObject
import com.example.aimessengerapp.ChatModel.ChatRepository
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.ChatNameModel.ChatEntityRepository
import com.example.aimessengerapp.RAGRepositories.RAGObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

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

    private val _selectedChat = MutableStateFlow<ChatEntity?>(null) // Храним текущий выбранный чат
    val selectedChat: StateFlow<ChatEntity?> = _selectedChat

    private val _currentChatIndex = MutableStateFlow<Int?>(null) // Храним индекс текущего чата
    val currentChatIndex: StateFlow<Int?> = _currentChatIndex

    fun determineChatToSelect(){
        viewModelScope.launch {
            val chatList = entityRepository.getAllChats().first()

            if (chatList.isEmpty()){
                val newChat = ChatEntity(name = "Новый чат 0", indexAt = 0, clicks = 0)
                insertChat(newChat)
                _selectedChat.value = newChat
                _currentChatIndex.value = 0
                return@launch
            }

            val lastChat = chatList.last()
            val messages1 = chatRepository.getMessages(lastChat.indexAt).first()
            if (messages1.isEmpty()){
                _selectedChat.value = lastChat
                _currentChatIndex.value = lastChat.indexAt
            }
            else {
                val newChat = ChatEntity(
                    name = "Новый чат ${chatList.size}",
                    indexAt = chatList.size,
                    clicks = 0
                )
                insertChat(newChat)
                _selectedChat.value = newChat
                _currentChatIndex.value = newChat.indexAt
            }
        }
    }
    //private var numberOfChat = 0

    init {
        load()
    }

    private fun load() {
        getAllChats()
    }

    fun getAllChats(){
        viewModelScope.launch {
            entityRepository.getAllChats().collectLatest { list ->
                _chats.clear()
                list.forEach { chatName ->
                    _chats.add(chatName)
                }
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
}