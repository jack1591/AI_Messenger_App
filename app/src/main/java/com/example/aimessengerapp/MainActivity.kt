package com.example.aimessengerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.RAGRepositories.GoalRepository
import com.example.aimessengerapp.RAGRepositories.LocationRepository
import com.example.aimessengerapp.RAGRepositories.PersonRepository
import com.example.aimessengerapp.View.MessengerPage2
import com.example.aimessengerapp.ViewModel.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAGViewModel
import com.example.aimessengerapp.ViewModel.RAGViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val database = AppDatabase.getDatabase(this)
        val personRepository = PersonRepository(database.personDao())
        val locationRepository = LocationRepository(database.locationDao())
        val goalRepository = GoalRepository(database.goalDao())

        val messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        val ragViewModel = ViewModelProvider(
            this,
            RAGViewModelFactory(personRepository, locationRepository, goalRepository)
        )[RAGViewModel::class.java]
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessengerPage2(messageViewModel,chatViewModel, ragViewModel)
        }
    }
}