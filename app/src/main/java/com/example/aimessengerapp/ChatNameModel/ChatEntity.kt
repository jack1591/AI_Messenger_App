package com.example.aimessengerapp.ChatNameModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val indexAt : String,
    val clicks: Int = 0
)