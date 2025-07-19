package com.remindme.app.data.repository

import androidx.lifecycle.LiveData
import com.remindme.app.data.dao.UserProgressDao
import com.remindme.app.data.model.UserProgress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProgressRepository @Inject constructor(
    private val userProgressDao: UserProgressDao
) {
    fun getUserProgress(): LiveData<UserProgress> = userProgressDao.getUserProgress()
    
    suspend fun insertUserProgress(userProgress: UserProgress) = userProgressDao.insertUserProgress(userProgress)
    
    suspend fun updateUserProgress(userProgress: UserProgress) = userProgressDao.updateUserProgress(userProgress)
    
    suspend fun addExperience(experience: Int) = userProgressDao.addExperience(experience)
    
    suspend fun calculateAndUpdateLevel() {
        val currentProgress = userProgressDao.getUserProgress().value ?: UserProgress()
        val currentLevel = currentProgress.level
        val currentExperience = currentProgress.experience
        
        // Calculate new level based on experience
        var newLevel = currentLevel
        for (level in 1..10) {
            val requiredExp = UserProgress.getExperienceForLevel(level)
            if (currentExperience >= requiredExp) {
                newLevel = level
            } else {
                break
            }
        }
        
        if (newLevel != currentLevel) {
            userProgressDao.updateProgress(currentExperience, newLevel, currentProgress.remindersCompleted)
        }
    }
} 