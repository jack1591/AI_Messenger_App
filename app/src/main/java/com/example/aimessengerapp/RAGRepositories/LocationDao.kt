package com.example.aimessengerapp.RAGRepositories

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations():Flow<List<Location>>

    @Insert
    suspend fun insertLocation(location: Location)
}