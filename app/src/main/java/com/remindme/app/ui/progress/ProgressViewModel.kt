package com.remindme.app.ui.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindme.app.data.model.Reminder
import com.remindme.app.data.model.UserProgress
import com.remindme.app.data.repository.ReminderRepository
import com.remindme.app.data.repository.UserProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val userProgressRepository: UserProgressRepository,
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _userProgress = MutableLiveData<UserProgress>()
    val userProgress: LiveData<UserProgress> = _userProgress

    private val _allReminders = MutableLiveData<List<Reminder>>()
    val allReminders: LiveData<List<Reminder>> = _allReminders

    private val _statistics = MutableLiveData<ProgressStatistics>()
    val statistics: LiveData<ProgressStatistics> = _statistics

    init {
        loadUserProgress()
        loadAllReminders()
    }

    private fun loadUserProgress() {
        viewModelScope.launch {
            try {
                userProgressRepository.getUserProgress().observeForever { progress ->
                    _userProgress.value = progress
                    calculateStatistics()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadAllReminders() {
        viewModelScope.launch {
            try {
                reminderRepository.getAllReminders().observeForever { reminders ->
                    _allReminders.value = reminders
                    calculateStatistics()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun calculateStatistics() {
        val progress = _userProgress.value ?: UserProgress()
        val reminders = _allReminders.value ?: emptyList()

        val totalReminders = reminders.size
        val completedReminders = reminders.count { it.isCompleted }
        val completionRate = if (totalReminders > 0) {
            (completedReminders * 100) / totalReminders
        } else 0

        val statistics = ProgressStatistics(
            totalReminders = totalReminders,
            completedReminders = completedReminders,
            completionRate = completionRate,
            currentLevel = progress.level,
            currentExperience = progress.experience,
            nextLevelExperience = UserProgress.getExperienceForLevel(progress.level + 1),
            levelName = UserProgress.getLevelName(progress.level)
        )

        _statistics.value = statistics
    }

    data class ProgressStatistics(
        val totalReminders: Int,
        val completedReminders: Int,
        val completionRate: Int,
        val currentLevel: Int,
        val currentExperience: Int,
        val nextLevelExperience: Int,
        val levelName: String
    )
} 