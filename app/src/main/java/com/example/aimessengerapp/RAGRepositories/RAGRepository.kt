package com.example.aimessengerapp.RAGRepositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// репозиторий для работы с rag-шаблонами
class RAGRepository(private val ragDao: RAGDao) {
    fun get(): Flow<List<RAGObject>> {
        return ragDao.getAllObjects()
    }

    suspend fun insert(ragObject: RAGObject){
        withContext(Dispatchers.IO) {
            ragDao.insert(ragObject)
        }
    }

    suspend fun delete(ragObject: RAGObject){
        withContext(Dispatchers.IO) {
            ragDao.delete(ragObject)
        }
    }

    suspend fun update(ragObject: RAGObject){
        withContext(Dispatchers.IO) {
            ragDao.update(ragObject)
        }
    }

}