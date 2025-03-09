package com.example.aimessengerapp.RAGRepositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//работа с rag-шаблонами
@Dao
interface RAGDao {

    // получить все rag-шаблоны из бд
    @Query("SELECT * FROM ragObjects")
    fun getAllObjects(): Flow<List<RAGObject>>

    //добавить шаблон
    @Insert
    suspend fun insert(ragObject: RAGObject)

    //удалить шаблон
    @Delete
    suspend fun delete(ragObject: RAGObject)

    //изменить шаблон (изменить имя)
    @Update
    suspend fun update(ragObject: RAGObject)

}