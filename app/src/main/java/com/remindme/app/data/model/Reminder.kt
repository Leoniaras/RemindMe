package com.remindme.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.remindme.app.data.converter.DateConverter
import java.util.Date

@Entity(tableName = "reminders")
@TypeConverters(DateConverter::class)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val reminderTime: Date,
    val isCompleted: Boolean = false,
    val createdAt: Date = Date(),
    val completedAt: Date? = null
) 