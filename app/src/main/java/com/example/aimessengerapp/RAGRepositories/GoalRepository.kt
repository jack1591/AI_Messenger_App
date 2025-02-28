package com.example.aimessengerapp.RAGRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GoalRepository(private val goalDao: GoalDao) {
    fun getGoals(): Flow<List<Goal>>{
        return goalDao.getAllGoals()
    }

    suspend fun addGoal(goal: Goal){
        withContext(Dispatchers.IO) {
            goalDao.insertGoal(goal)
        }
    }

    suspend fun deleteGoal(goal: Goal){
        withContext(Dispatchers.IO) {
            goalDao.deleteGoal(goal)
        }
    }

    suspend fun updateGoal(goal: Goal){
        withContext(Dispatchers.IO) {
            goalDao.updateGoal(goal)
        }
    }
}