package com.example.aimessengerapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.RAGRepositories.GoalRepository
import com.example.aimessengerapp.RAGRepositories.LocationRepository
import com.example.aimessengerapp.RAGRepositories.PersonRepository

class RAGViewModelFactory(
    private val personRepository: PersonRepository,
    private val locationRepository: LocationRepository,
    private val goalRepository: GoalRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RAGViewModel(personRepository, locationRepository, goalRepository) as T
    }
}