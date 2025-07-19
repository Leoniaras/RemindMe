package com.remindme.app.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindme.app.data.model.Reminder
import com.remindme.app.data.repository.ReminderRepository
import com.remindme.app.data.repository.UserProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val userProgressRepository: UserProgressRepository
) : ViewModel() {

    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> = _selectedDate

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> = _reminders

    private val _isReminderCompleted = MutableLiveData<Boolean>()
    val isReminderCompleted: LiveData<Boolean> = _isReminderCompleted

    private val _isReminderDeleted = MutableLiveData<Boolean>()
    val isReminderDeleted: LiveData<Boolean> = _isReminderDeleted

    init {
        _selectedDate.value = Date()
        loadRemindersForDate(Date())
    }

    fun setSelectedDate(date: Date) {
        _selectedDate.value = date
        loadRemindersForDate(date)
    }

    private fun loadRemindersForDate(date: Date) {
        viewModelScope.launch {
            try {
                val remindersList = reminderRepository.getRemindersForDate(date).value ?: emptyList()
                _reminders.value = remindersList
            } catch (e: Exception) {
                _reminders.value = emptyList()
            }
        }
    }

    fun markReminderAsCompleted(reminderId: Long) {
        viewModelScope.launch {
            try {
                reminderRepository.markAsCompleted(reminderId)
                userProgressRepository.addExperience(10) // 10 XP for completing a reminder
                userProgressRepository.calculateAndUpdateLevel()
                _isReminderCompleted.value = true
                loadRemindersForDate(_selectedDate.value ?: Date())
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                reminderRepository.deleteReminder(reminder)
                _isReminderDeleted.value = true
                loadRemindersForDate(_selectedDate.value ?: Date())
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun resetReminderCompleted() {
        _isReminderCompleted.value = false
    }

    fun resetReminderDeleted() {
        _isReminderDeleted.value = false
    }
} 