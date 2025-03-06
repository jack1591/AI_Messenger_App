package com.example.aimessengerapp.ChatNameModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatEntityDao {
    @Insert()
    suspend fun insertChat(chat: ChatEntity)

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Query("SELECT * FROM chats ORDER BY indexAt ASC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats ORDER BY clicks DESC")
    fun getPopularChats(): Flow<List<ChatEntity>>

    @Query("UPDATE chats SET clicks = clicks + 1 WHERE id = :chatId")
    suspend fun incrementClick(chatId: Int)
}