package com.remindme.app.data.repository

import androidx.lifecycle.LiveData
import com.remindme.app.data.dao.ReminderDao
import com.remindme.app.data.model.Reminder
import com.remindme.app.service.NotificationService
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao,
    private val notificationService: NotificationService
) {
    fun getAllReminders(): LiveData<List<Reminder>> = reminderDao.getAllReminders()
    
    fun getActiveReminders(): LiveData<List<Reminder>> = reminderDao.getActiveReminders()
    
    fun getRemindersForDate(date: Date): LiveData<List<Reminder>> = reminderDao.getRemindersForDate(date)
    
    suspend fun getReminderById(id: Long): Reminder? = reminderDao.getReminderById(id)
    
    suspend fun insertReminder(reminder: Reminder): Long {
        val reminderId = reminderDao.insertReminder(reminder)
        // Schedule notification for the reminder
        val reminderWithId = reminder.copy(id = reminderId)
        notificationService.scheduleReminder(reminderWithId)
        return reminderId
    }
    
    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
        // Reschedule notification
        notificationService.cancelReminder(reminder.id)
        notificationService.scheduleReminder(reminder)
    }
    
    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
        // Cancel notification
        notificationService.cancelReminder(reminder.id)
    }
    
    suspend fun markAsCompleted(id: Long) {
        reminderDao.markAsCompleted(id)
        // Cancel notification since reminder is completed
        notificationService.cancelReminder(id)
    }
    
    suspend fun getCompletedRemindersCount(): Int = reminderDao.getCompletedRemindersCount()
} 