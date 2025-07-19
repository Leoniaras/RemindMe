package com.remindme.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.remindme.app.data.converter.DateConverter
import com.remindme.app.data.dao.ReminderDao
import com.remindme.app.data.dao.UserProgressDao
import com.remindme.app.data.model.Reminder
import com.remindme.app.data.model.UserProgress

@Database(
    entities = [Reminder::class, UserProgress::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun userProgressDao(): UserProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "remindme_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 