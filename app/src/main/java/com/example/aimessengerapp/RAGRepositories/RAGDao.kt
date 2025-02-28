package com.example.aimessengerapp.RAGRepositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RAGDao {
    @Query("SELECT * FROM ragObjects")
    fun getAllObjects(): Flow<List<RAGObject>>

    @Insert
    suspend fun insert(ragObject: RAGObject)

    @Delete
    suspend fun delete(ragObject: RAGObject)

    @Update
    suspend fun update(ragObject: RAGObject)

}