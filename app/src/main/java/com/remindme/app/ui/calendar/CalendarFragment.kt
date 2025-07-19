package com.remindme.app.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.remindme.app.R
import com.remindme.app.databinding.FragmentCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var reminderAdapter: ReminderAdapter

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCalendarView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        reminderAdapter = ReminderAdapter(
            onCompleteClick = { reminderId ->
                viewModel.markReminderAsCompleted(reminderId)
            },
            onDeleteClick = { reminder ->
                viewModel.deleteReminder(reminder)
            }
        )

        binding.remindersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reminderAdapter
        }
    }

    private fun setupCalendarView() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            viewModel.setSelectedDate(selectedDate)
        }
    }

    private fun observeViewModel() {
        viewModel.reminders.observe(viewLifecycleOwner) { reminders ->
            reminderAdapter.submitList(reminders)
            updateNoRemindersVisibility(reminders.isEmpty())
        }

        viewModel.isReminderCompleted.observe(viewLifecycleOwner) { isCompleted ->
            if (isCompleted) {
                Toast.makeText(context, "Reminder completed! +10 XP", Toast.LENGTH_SHORT).show()
                viewModel.resetReminderCompleted()
            }
        }

        viewModel.isReminderDeleted.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                Toast.makeText(context, "Reminder deleted", Toast.LENGTH_SHORT).show()
                viewModel.resetReminderDeleted()
            }
        }
    }

    private fun updateNoRemindersVisibility(isEmpty: Boolean) {
        binding.noRemindersText.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 