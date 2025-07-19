package com.remindme.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey
    val id: Int = 1, // Only one user progress record
    val experience: Int = 0,
    val level: Int = 1,
    val remindersCompleted: Int = 0
) {
    companion object {
        // Experience required for each level (increasing exponentially)
        val LEVEL_EXPERIENCE = mapOf(
            1 to 0,      // Level 1: 0 XP
            2 to 10,     // Level 2: 10 XP
            3 to 25,     // Level 3: 25 XP
            4 to 50,     // Level 4: 50 XP
            5 to 100,    // Level 5: 100 XP
            6 to 200,    // Level 6: 200 XP
            7 to 400,    // Level 7: 400 XP
            8 to 800,    // Level 8: 800 XP
            9 to 1600,   // Level 9: 1600 XP
            10 to 3200   // Level 10: 3200 XP
        )
        
        fun getExperienceForLevel(level: Int): Int {
            return LEVEL_EXPERIENCE[level] ?: 0
        }
        
        fun getLevelName(level: Int): String {
            return when (level) {
                1 -> "Beginner"
                2 -> "Novice"
                3 -> "Apprentice"
                4 -> "Student"
                5 -> "Learner"
                6 -> "Practitioner"
                7 -> "Adept"
                8 -> "Expert"
                9 -> "Master"
                10 -> "Grandmaster"
                else -> "Unknown"
            }
        }
    }
} 