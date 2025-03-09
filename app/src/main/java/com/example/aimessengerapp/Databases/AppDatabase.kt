package com.example.aimessengerapp.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aimessengerapp.RAGRepositories.RAGDao
import com.example.aimessengerapp.RAGRepositories.RAGObject

// база данных для работы с rag-шаблонами
private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE ragObjects_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "type TEXT NOT NULL, " +
                    "isFavorite INTEGER NOT NULL DEFAULT 0)"
        )

        // Удаляем старую таблицу
        database.execSQL("DROP TABLE ragObjects")

        // Переименовываем новую таблицу
        database.execSQL("ALTER TABLE ragObjects_new RENAME TO ragObjects")
    }
}

@Database(entities = [RAGObject::class],version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ragDao(): RAGDao

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
                    AppDatabase::class.java, "ragObjects")
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}