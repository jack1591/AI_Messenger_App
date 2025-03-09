package com.example.aimessengerapp.ChatModel

import androidx.room.Entity
import androidx.room.PrimaryKey

//Класс сообщения
@Entity(tableName = "messages")
data class ChatObject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String, //содержание
    val type : String, //тип - запрос/ответ
    val chatId: Int // номер чата, в котором было отправлено сообщение
)

