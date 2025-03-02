package com.example.aimessengerapp.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.ChatNameModel.ChatEntityDao

@Database(entities = [ChatEntity::class],version = 1, exportSchema = false)
abstract class ChatEntityDatabase: RoomDatabase() {
    abstract fun chatEntityDao(): ChatEntityDao

    companion object{
        @Volatile
        private var INSTANCE: ChatEntityDatabase? = null

        fun getDatabase(context: Context): ChatEntityDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    ChatEntityDatabase::class.java, "chats")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}