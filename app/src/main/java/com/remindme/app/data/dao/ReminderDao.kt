package com.remindme.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.remindme.app.data.model.Reminder
import java.util.Date

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY reminderTime ASC")
    fun getAllReminders(): LiveData<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE isCompleted = 0 ORDER BY reminderTime ASC")
    fun getActiveReminders(): LiveData<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE DATE(reminderTime/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') ORDER BY reminderTime ASC")
    fun getRemindersForDate(date: Date): LiveData<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: Long): Reminder?
    
    @Insert
    suspend fun insertReminder(reminder: Reminder): Long
    
    @Update
    suspend fun updateReminder(reminder: Reminder)
    
    @Delete
    suspend fun deleteReminder(reminder: Reminder)
    
    @Query("UPDATE reminders SET isCompleted = 1, completedAt = :completedAt WHERE id = :id")
    suspend fun markAsCompleted(id: Long, completedAt: Date = Date())
    
    @Query("SELECT COUNT(*) FROM reminders WHERE isCompleted = 1")
    suspend fun getCompletedRemindersCount(): Int
} 