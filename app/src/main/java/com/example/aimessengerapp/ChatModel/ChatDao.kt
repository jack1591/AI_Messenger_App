package com.example.aimessengerapp.ChatModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.aimessengerapp.RAGRepositories.RAGObject
import kotlinx.coroutines.flow.Flow

//получение и отправка сообщений
@Dao
interface ChatDao {

    //получение сообщений по чату
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY id ASC")
    fun getMessagesByChatId(chatId: Int): Flow<List<ChatObject>>

    //удаление всех сообщений (при удалении чата)
    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChatId(chatId: Int)

    //добавление сообщения в бд
    @Insert
    suspend fun insertMessage(message: ChatObject)

}