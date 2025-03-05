package com.example.aimessengerapp.ChatModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class ChatObject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val type : String,
    val chatId: Int
){

    /*
    fun doesMatchSearchQuery(query: String): Boolean {

        val matchingCombinations = listOf(
            "$content"
        )

        return matchingCombinations.any{
            it.contains(query, ignoreCase = true)
        }
    }
     */

}

