package com.example.aimessengerapp.RAGRepositories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ragObjects")
data class RAGObject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type : String,
    val name: String
)