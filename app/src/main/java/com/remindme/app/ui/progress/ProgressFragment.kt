package com.remindme.app.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.remindme.app.R
import com.remindme.app.databinding.FragmentProgressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProgressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.statistics.observe(viewLifecycleOwner) { statistics ->
            updateProgressUI(statistics)
        }
    }

    private fun updateProgressUI(statistics: ProgressViewModel.ProgressStatistics) {
        // Update level information
        binding.levelText.text = getString(R.string.level_label, statistics.currentLevel)
        binding.levelNameText.text = statistics.levelName

        // Update experience information
        binding.experienceText.text = getString(
            R.string.experience_label,
            statistics.currentExperience,
            statistics.nextLevelExperience
        )

        // Update progress bar
        val progress = if (statistics.nextLevelExperience > 0) {
            val currentLevelExp = statistics.currentExperience
            val nextLevelExp = statistics.nextLevelExperience
            val progressInLevel = currentLevelExp - statistics.currentLevel * 10 // Approximate
            val expNeededForNextLevel = nextLevelExp - currentLevelExp
            if (expNeededForNextLevel > 0) {
                (progressInLevel * 100) / expNeededForNextLevel
            } else {
                100
            }
        } else {
            100
        }
        binding.experienceProgress.progress = progress.coerceIn(0, 100)

        // Update next level text
        val expNeeded = statistics.nextLevelExperience - statistics.currentExperience
        binding.nextLevelText.text = getString(
            R.string.next_level_progress,
            if (expNeeded > 0) expNeeded / 10 else 0 // Convert XP to reminders (10 XP per reminder)
        )

        // Update statistics
        binding.totalRemindersText.text = statistics.totalReminders.toString()
        binding.completedRemindersText.text = statistics.completedReminders.toString()
        binding.completionRateText.text = "${statistics.completionRate}%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 