package com.example.aimessengerapp.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aimessengerapp.ChatModel.ChatDao
import com.example.aimessengerapp.ChatModel.ChatObject

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE chatObjects_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "content TEXT NOT NULL, " +
                    "type TEXT NOT NULL, " +
                    "chatId INTEGER NOT NULL)"
        )

        database.execSQL("DROP TABLE messages")

        database.execSQL("ALTER TABLE chatObjects_new RENAME TO messages")
    }
}

@Database(entities = [ChatObject::class],version = 4, exportSchema = false)
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
                    ChatDatabase::class.java, "chatDatabase")
                    .addMigrations(MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}