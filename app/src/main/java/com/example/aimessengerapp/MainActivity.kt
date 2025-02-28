package com.example.aimessengerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.RAGRepositories.RAGRepository
import com.example.aimessengerapp.View.MessengerPage2
import com.example.aimessengerapp.ViewModel.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAGViewModel
import com.example.aimessengerapp.ViewModel.RAGViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val database = AppDatabase.getDatabase(this)

        val repository = RAGRepository(database.ragDao())
        val ragViewModel = ViewModelProvider(
            this,
            RAGViewModelFactory(repository)
        )[RAGViewModel::class.java]

        val messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessengerPage2(messageViewModel,chatViewModel, ragViewModel)
        }
    }
}