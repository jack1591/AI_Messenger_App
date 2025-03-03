package com.example.aimessengerapp.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.ChatNameModel.ChatEntityDao

private val MIGRATION_12_13 = object : Migration(12, 13) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE chats_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "indexAt INTEGER NOT NULL DEFAULT 0, " +
                    "clicks INTEGER NOT NULL DEFAULT 0)"
        )

        // Удаляем старую таблицу
        database.execSQL("DROP TABLE chats")

        // Переименовываем новую таблицу
        database.execSQL("ALTER TABLE chats_new RENAME TO chats")
    }
}


@Database(entities = [ChatEntity::class],version = 13, exportSchema = false)
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
                    .addMigrations(MIGRATION_12_13)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}