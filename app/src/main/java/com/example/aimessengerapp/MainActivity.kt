package com.example.aimessengerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.ChatModel.ChatRepository
import com.example.aimessengerapp.Databases.AppDatabase
import com.example.aimessengerapp.Databases.ChatDatabase
import com.example.aimessengerapp.RAGRepositories.RAGRepository
import com.example.aimessengerapp.View.MessengerPage2
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModelFactory
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val database = AppDatabase.getDatabase(this)
        val chatDatabase = ChatDatabase.getDatabase(this)

        val repository = RAGRepository(database.ragDao())
        val chatRepository = ChatRepository(chatDatabase.chatDao())
   
        val ragViewModel = ViewModelProvider(
            this,
            RAGViewModelFactory(repository)
        )[RAGViewModel::class.java]

        val chatViewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(chatRepository)
        )[ChatViewModel::class.java]

        val messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessengerPage2(messageViewModel,chatViewModel, ragViewModel)
        }
    }
}