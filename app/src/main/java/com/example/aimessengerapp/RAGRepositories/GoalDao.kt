package com.example.aimessengerapp.RAGRepositories

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals")
    fun getAllGoals():Flow<List<Goal>>

    @Insert
    suspend fun insertGoal(goal: Goal)
}