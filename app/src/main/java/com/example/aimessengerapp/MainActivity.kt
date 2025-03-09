package com.example.aimessengerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.aimessengerapp.ChatModel.ChatRepository
import com.example.aimessengerapp.ChatNameModel.ChatEntityRepository
import com.example.aimessengerapp.Databases.AppDatabase
import com.example.aimessengerapp.Databases.ChatDatabase
import com.example.aimessengerapp.Databases.ChatEntityDatabase
import com.example.aimessengerapp.RAGRepositories.RAGRepository
import com.example.aimessengerapp.View.MainPage.MessengerPage2
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModelFactory
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModelFactory
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    //создание парсвера из голоса в текст
    val voiceToTextParser by lazy{
        VoiceToTextParser(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //создание баз данных
        val database = AppDatabase.getDatabase(this) //для работы с api
        val chatDatabase = ChatDatabase.getDatabase(this) // для rag шаблонов
        val chatEntityDatabase = ChatEntityDatabase.getDatabase(this) //для сообщений и чатов

        val repository = RAGRepository(database.ragDao()) // репозиторий для rag-шаблонов
        val chatRepository = ChatRepository(chatDatabase.chatDao()) //репозиторий для работы с сообщениями
        val entityRepository = ChatEntityRepository(chatEntityDatabase.chatEntityDao()) //репозиторий для работы с чатами

        //ViewModel для работы с rag
        val ragViewModel = ViewModelProvider(
            this,
            RAGViewModelFactory(repository)
        )[RAGViewModel::class.java]

        //ViewModel для работы с сообщениями и чатами
        val chatViewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(chatRepository,entityRepository)
        )[ChatViewModel::class.java]

        //ViewModel для работы с API
        val messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]

        super.onCreate(savedInstanceState)

        //создание канала для отправки сообщений
        createNotificationChannel(this)

        //проверка и запрос на разрешение отправки уведомлений
        checkAndRequestNotificationPermission(this)

        //установка времени уведомления
        scheduleAlarm(this)

        enableEdgeToEdge()
        setContent {

            // Флаг, указывающий, можно ли записывать звук (true - разрешено, false - нет)
            var canRecord by remember{
                mutableStateOf(false)
            }

            // Запрос разрешения на использование микрофона
            var recordAudioLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(), // Контракт для запроса разрешения
                onResult = { isGranted ->
                    canRecord = isGranted // Устанавливаем canRecord в true, если разрешение получено
                }
            )

            // Автоматически запрашиваем разрешение при первом запуске
            LaunchedEffect(key1 = recordAudioLauncher) {
                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }

            //основной экран
            MessengerPage2(messageViewModel,chatViewModel, ragViewModel, voiceToTextParser)
        }
    }
}

