package com.remindme.app.ui.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.remindme.app.R
import com.remindme.app.databinding.FragmentCreateReminderBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateReminderFragment : Fragment() {

    private var _binding: FragmentCreateReminderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateReminderViewModel by viewModels()

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.dateButton.setOnClickListener {
            showDatePicker()
        }

        binding.timeButton.setOnClickListener {
            showTimePicker()
        }

        binding.createButton.setOnClickListener {
            createReminder()
        }
    }

    private fun observeViewModel() {
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            binding.dateButton.text = dateFormat.format(date)
            updateSelectedDateTimeText()
        }

        viewModel.selectedTime.observe(viewLifecycleOwner) { time ->
            binding.timeButton.text = timeFormat.format(time)
            updateSelectedDateTimeText()
        }

        viewModel.isReminderCreated.observe(viewLifecycleOwner) { isCreated ->
            if (isCreated) {
                Toast.makeText(context, getString(R.string.reminder_created_success), Toast.LENGTH_SHORT).show()
                clearForm()
                viewModel.resetReminderCreated()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time
                viewModel.setSelectedDate(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }.time
                viewModel.setSelectedTime(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun createReminder() {
        val title = binding.titleEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        viewModel.createReminder(title, description)
    }

    private fun updateSelectedDateTimeText() {
        val date = viewModel.selectedDate.value
        val time = viewModel.selectedTime.value

        if (date != null && time != null) {
            val combinedDateTime = Calendar.getInstance().apply {
                val dateCal = Calendar.getInstance().apply { time = date }
                val timeCal = Calendar.getInstance().apply { time = time }
                set(
                    dateCal.get(Calendar.YEAR),
                    dateCal.get(Calendar.MONTH),
                    dateCal.get(Calendar.DAY_OF_MONTH),
                    timeCal.get(Calendar.HOUR_OF_DAY),
                    timeCal.get(Calendar.MINUTE),
                    0
                )
            }.time

            val combinedFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
            binding.selectedDatetimeText.text = combinedFormat.format(combinedDateTime)
        }
    }

    private fun clearForm() {
        binding.titleEditText.text?.clear()
        binding.descriptionEditText.text?.clear()
        binding.selectedDatetimeText.text = ""
        viewModel.setSelectedDate(Date())
        viewModel.setSelectedTime(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 