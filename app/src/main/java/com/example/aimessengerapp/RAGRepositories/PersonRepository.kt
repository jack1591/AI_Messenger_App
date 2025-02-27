package com.example.aimessengerapp.RAGRepositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PersonRepository(private val personDao: PersonDao) {
    fun getPersons(): Flow<List<Person>> {
        return personDao.getAllPersons()
    }

    suspend fun addPerson(person: Person){
        withContext(Dispatchers.IO) {
            personDao.insertPerson(person)
        }
    }

    suspend fun deletePerson(person: Person){
        withContext(Dispatchers.IO) {
            personDao.deletePerson(person)
        }
    }

    suspend fun updatePerson(person: Person){
        withContext(Dispatchers.IO) {
            personDao.updatePerson(person)
        }
    }
}