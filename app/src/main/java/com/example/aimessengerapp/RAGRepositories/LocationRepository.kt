package com.example.aimessengerapp.RAGRepositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocationRepository(private val locationDao: LocationDao) {
    fun getLocations(): Flow<List<Location>>{
        return locationDao.getAllLocations()
    }

    suspend fun addLocation(location: Location){
        withContext(Dispatchers.IO) {
            locationDao.insertLocation(location)
        }
    }
}