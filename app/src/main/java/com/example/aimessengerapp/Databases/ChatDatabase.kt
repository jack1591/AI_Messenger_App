package com.example.aimessengerapp.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aimessengerapp.ChatModel.ChatDao
import com.example.aimessengerapp.ChatModel.ChatObject

@Database(entities = [ChatObject::class],version = 1, exportSchema = false)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun chatDao(): ChatDao

    companion object{
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun getDatabase(context: Context): ChatDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    ChatDatabase::class.java, "chatObjects")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}