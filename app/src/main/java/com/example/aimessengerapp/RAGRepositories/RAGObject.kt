package com.example.aimessengerapp.RAGRepositories

import androidx.room.Entity
import androidx.room.PrimaryKey

//rag-шаблон
@Entity(tableName = "ragObjects")
data class RAGObject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type : String, //тип шаблона (человек, цель, окружение)
    val name: String, //имя щшаблона
    val isFavorite: Boolean //является ли избранным
)