package com.example.aimessengerapp.ChatNameModel

import com.example.aimessengerapp.ChatModel.ChatObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatEntityRepository(private val chatEntityDao: ChatEntityDao) {


    fun getAllChats(): Flow<List<ChatEntity>> {
        return chatEntityDao.getAllChats()
    }

    fun getAllChatsByPopularity(): Flow<List<ChatEntity>> {
        return chatEntityDao.getPopularChats()
    }

    suspend fun insertChat(chatEntity: ChatEntity){
        withContext(Dispatchers.IO) {
            chatEntityDao.insertChat(chatEntity)
        }
    }

    suspend fun updateChat(chatEntity: ChatEntity){
        withContext(Dispatchers.IO) {
            chatEntityDao.updateChat(chatEntity)
        }
    }

    suspend fun deleteChat(chat: ChatEntity){
        withContext(Dispatchers.IO) {
            chatEntityDao.deleteChat(chat)
        }
    }

    suspend fun incrementChatClicks(chatId: Int) {
        chatEntityDao.incrementClick(chatId)
    }
}