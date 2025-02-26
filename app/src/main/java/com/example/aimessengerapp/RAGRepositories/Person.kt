package com.example.aimessengerapp.RAGRepositories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons") // ✅ Создаём таблицу `persons`
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)