package com.example.aimessengerapp.RAGRepositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations():Flow<List<Location>>

    @Delete
    suspend fun deleteLocation(location:Location)

    @Update
    suspend fun updateLocation(location: Location)

    @Insert
    suspend fun insertLocation(location: Location)
}