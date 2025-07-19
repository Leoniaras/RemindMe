package com.remindme.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.remindme.app.data.model.UserProgress

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgress(): LiveData<UserProgress>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProgress(userProgress: UserProgress)
    
    @Update
    suspend fun updateUserProgress(userProgress: UserProgress)
    
    @Query("UPDATE user_progress SET experience = :experience, level = :level, remindersCompleted = :remindersCompleted WHERE id = 1")
    suspend fun updateProgress(experience: Int, level: Int, remindersCompleted: Int)
    
    @Query("UPDATE user_progress SET experience = experience + :experience, remindersCompleted = remindersCompleted + 1 WHERE id = 1")
    suspend fun addExperience(experience: Int)
} 