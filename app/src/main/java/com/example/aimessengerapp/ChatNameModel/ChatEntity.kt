package com.example.aimessengerapp.ChatNameModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    val indexAt : Int,
    val clicks: Int = 0,
    val isFavorite: Boolean
)