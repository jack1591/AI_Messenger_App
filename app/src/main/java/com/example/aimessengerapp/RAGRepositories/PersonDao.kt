package com.example.aimessengerapp.RAGRepositories

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PersonDao {
    @Query("SELECT * FROM persons")
    fun getAllPersons(): Flow<List<Person>>

    @Insert
    suspend fun insertPerson(person: Person)
}