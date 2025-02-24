package com.example.aimessengerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.View.MessengerPage
import com.example.aimessengerapp.ViewModel.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessengerPage(messageViewModel,chatViewModel)
        }
    }
}