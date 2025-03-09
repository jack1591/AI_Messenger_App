package com.example.aimessengerapp.ChatNameModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//Работа с чатами
@Dao
interface ChatEntityDao {

    //добавление чата
    @Insert()
    suspend fun insertChat(chat: ChatEntity)

    //изменение чата (изменение названия, добавление в избранное)
    @Update
    suspend fun updateChat(chat: ChatEntity)

    //удаление чата
    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    //получение всех чатов по дате
    @Query("SELECT * FROM chats ORDER BY indexAt ASC")
    fun getAllChats(): Flow<List<ChatEntity>>

    //получение всех чатов по популярности
    @Query("SELECT * FROM chats ORDER BY clicks DESC")
    fun getPopularChats(): Flow<List<ChatEntity>>

    /*
    //увеличение числа кликов в чате, номер которого равен chatId
    @Query("UPDATE chats SET clicks = clicks + 1 WHERE id = :chatId")
    suspend fun incrementClick(chatId: Int)

     */
}