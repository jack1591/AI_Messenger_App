package com.example.aimessengerapp.ChatNameModel

import androidx.room.Entity
import androidx.room.PrimaryKey

//Класс для чатов
@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String, //имя чата
    val indexAt : Int, //номер чата
    val clicks: Int = 0, //число кликов в текущем чате
    val isFavorite: Boolean //находится ли в избранном
)