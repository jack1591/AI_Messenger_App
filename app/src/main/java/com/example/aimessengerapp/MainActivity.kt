package com.example.aimessengerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.ChatModel.ChatRepository
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.ChatNameModel.ChatEntityRepository
import com.example.aimessengerapp.Databases.AppDatabase
import com.example.aimessengerapp.Databases.ChatDatabase
import com.example.aimessengerapp.Databases.ChatEntityDatabase
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
        val chatEntityDatabase = ChatEntityDatabase.getDatabase(this)

        val repository = RAGRepository(database.ragDao())
        val chatRepository = ChatRepository(chatDatabase.chatDao())
        val entityRepository = ChatEntityRepository(chatEntityDatabase.chatEntityDao())

        val ragViewModel = ViewModelProvider(
            this,
            RAGViewModelFactory(repository)
        )[RAGViewModel::class.java]

        val chatViewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(chatRepository,entityRepository)
        )[ChatViewModel::class.java]

        val messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessengerPage2(messageViewModel,chatViewModel, ragViewModel)
        }
    }
}