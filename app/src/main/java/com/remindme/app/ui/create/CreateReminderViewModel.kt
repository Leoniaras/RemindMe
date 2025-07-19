package com.remindme.app.ui.create

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
class CreateReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val userProgressRepository: UserProgressRepository
) : ViewModel() {

    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> = _selectedDate

    private val _selectedTime = MutableLiveData<Date>()
    val selectedTime: LiveData<Date> = _selectedTime

    private val _isReminderCreated = MutableLiveData<Boolean>()
    val isReminderCreated: LiveData<Boolean> = _isReminderCreated

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun setSelectedDate(date: Date) {
        _selectedDate.value = date
    }

    fun setSelectedTime(time: Date) {
        _selectedTime.value = time
    }

    fun createReminder(title: String, description: String?) {
        if (title.isBlank()) {
            _errorMessage.value = "Please enter a title for your reminder"
            return
        }

        val date = _selectedDate.value
        val time = _selectedTime.value

        if (date == null) {
            _errorMessage.value = "Please select a date"
            return
        }

        if (time == null) {
            _errorMessage.value = "Please select a time"
            return
        }

        // Combine date and time
        val calendar = java.util.Calendar.getInstance()
        val dateCalendar = java.util.Calendar.getInstance().apply { time = date }
        val timeCalendar = java.util.Calendar.getInstance().apply { time = time }

        calendar.set(
            dateCalendar.get(java.util.Calendar.YEAR),
            dateCalendar.get(java.util.Calendar.MONTH),
            dateCalendar.get(java.util.Calendar.DAY_OF_MONTH),
            timeCalendar.get(java.util.Calendar.HOUR_OF_DAY),
            timeCalendar.get(java.util.Calendar.MINUTE),
            0
        )

        val reminderTime = calendar.time

        // Check if the reminder time is in the past
        if (reminderTime.before(Date())) {
            _errorMessage.value = "Cannot set reminder for past date/time"
            return
        }

        val reminder = Reminder(
            title = title.trim(),
            description = description?.trim()?.takeIf { it.isNotBlank() },
            reminderTime = reminderTime
        )

        viewModelScope.launch {
            try {
                reminderRepository.insertReminder(reminder)
                _isReminderCreated.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create reminder: ${e.message}"
            }
        }
    }

    fun resetReminderCreated() {
        _isReminderCreated.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
} 