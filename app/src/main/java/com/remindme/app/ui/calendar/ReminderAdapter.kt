package com.remindme.app.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.remindme.app.data.model.Reminder
import com.remindme.app.databinding.ItemReminderBinding
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter(
    private val onCompleteClick: (Long) -> Unit,
    private val onDeleteClick: (Reminder) -> Unit
) : ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder>(ReminderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemReminderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReminderViewHolder(
        private val binding: ItemReminderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val timeFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())

        fun bind(reminder: Reminder) {
            binding.reminderTitle.text = reminder.title
            
            reminder.description?.let { description ->
                if (description.isNotBlank()) {
                    binding.reminderDescription.text = description
                    binding.reminderDescription.visibility = android.view.View.VISIBLE
                } else {
                    binding.reminderDescription.visibility = android.view.View.GONE
                }
            } ?: run {
                binding.reminderDescription.visibility = android.view.View.GONE
            }

            binding.reminderTime.text = timeFormat.format(reminder.reminderTime)

            // Show/hide complete button based on completion status
            binding.completeButton.visibility = if (reminder.isCompleted) {
                android.view.View.GONE
            } else {
                android.view.View.VISIBLE
            }

            binding.completeButton.setOnClickListener {
                onCompleteClick(reminder.id)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(reminder)
            }
        }
    }

    private class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem == newItem
        }
    }
} 