package com.example.aimessengerapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aimessengerapp.RAGRepositories.Goal
import com.example.aimessengerapp.RAGRepositories.GoalDao
import com.example.aimessengerapp.RAGRepositories.Location
import com.example.aimessengerapp.RAGRepositories.LocationDao
import com.example.aimessengerapp.RAGRepositories.Person
import com.example.aimessengerapp.RAGRepositories.PersonDao

@Database(entities = [Person::class, Location::class, Goal::class],version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun locationDao(): LocationDao
    abstract fun goalDao(): GoalDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "jetpack")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}