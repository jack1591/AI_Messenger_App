package com.example.aimessengerapp.ChatModel

import com.example.aimessengerapp.RAGRepositories.RAGDao
import com.example.aimessengerapp.RAGRepositories.RAGObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatRepository(private val chatDao: ChatDao) {
    fun getMessages(chatId: Int): Flow<List<ChatObject>> {
        return chatDao.getMessagesByChatId(chatId)
    }

    suspend fun insert(message: ChatObject){
        withContext(Dispatchers.IO) {
            chatDao.insertMessage(message)
        }
    }

}