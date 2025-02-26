package com.example.aimessengerapp.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimessengerapp.RAGRepositories.Goal
import com.example.aimessengerapp.RAGRepositories.GoalRepository
import com.example.aimessengerapp.RAGRepositories.Location
import com.example.aimessengerapp.RAGRepositories.LocationRepository
import com.example.aimessengerapp.RAGRepositories.Person
import com.example.aimessengerapp.RAGRepositories.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RAGViewModel (
    private val personRepository: PersonRepository,
    private val locationRepository: LocationRepository,
    private val goalRepository: GoalRepository
): ViewModel() {

    /*
    private val _goals = MutableLiveData<List<Goal>>()
    val goals: LiveData<List<Goal>> get() = _goals

    private val _locations = MutableLiveData<List<Location>>()
    val locations : LiveData<List<Location>> get() = _locations

    private val _persons = MutableLiveData<List<Person>>()
    val persons: LiveData<List<Person>> get() = _persons
     */
    private val _persons = MutableStateFlow<List<Person>>(emptyList()) // ✅ Используем Flow
    val persons: StateFlow<List<Person>> = _persons

    private val _locations = MutableStateFlow<List<Location>>(emptyList()) // ✅ Используем Flow
    val locations: StateFlow<List<Location>> = _locations

    private val _goals = MutableStateFlow<List<Goal>>(emptyList()) // ✅ Используем Flow
    val goals: StateFlow<List<Goal>> = _goals

    init {
        loadPersons()
        loadLocations()
        loadGoals()
    }

    private fun loadPersons(){
        viewModelScope.launch(Dispatchers.IO) { // ✅ Запускаем в фоне
            personRepository.getPersons().collectLatest { personsList ->
                _persons.value = personsList // ✅ Автоматически обновляет UI
            }
        }
    }

    private fun loadLocations(){
        viewModelScope.launch(Dispatchers.IO) { // ✅ Запускаем в фоне
            locationRepository.getLocations().collectLatest { locationsList ->
                _locations.value = locationsList // ✅ Автоматически обновляет UI
            }
        }

    }

    private fun loadGoals(){
        viewModelScope.launch(Dispatchers.IO) { // ✅ Запускаем в фоне
            goalRepository.getGoals().collectLatest { goalsList ->
                _goals.value = goalsList // ✅ Автоматически обновляет UI
            }
        }
    }

    fun addPerson(person: Person){
        viewModelScope.launch {
            personRepository.addPerson(person)
        }
    }

    fun addLocation(location: Location){
        viewModelScope.launch {
            locationRepository.addLocation(location)
        }
    }

    fun addGoal(goal: Goal){
        viewModelScope.launch {
            goalRepository.addGoal(goal)
        }
    }

    private var _isRAG = mutableStateOf<Boolean>(false)
    val isRAG = _isRAG

    fun changeRAG() {
        _isRAG.value = ! _isRAG.value
    }

    fun changeRAG_byvalue(_value: Boolean) {
        _isRAG.value = _value
    }
}