package dev.ykzza.chesstimer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ykzza.chesstimer.R
import dev.ykzza.chesstimer.databinding.FragmentSettingsBinding
import dev.ykzza.chesstimer.domain.TimeMode

class SettingsFragment : Fragment(), TimeModeAdapter.OnTimeModeClickListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding ?: throw RuntimeException("FragmentSettingsBinding is null")

    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val timeModeAdapter = TimeModeAdapter(this)
        binding.ibBack.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_timersFragment)
        }
        binding.btnNewTimeMode.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_addTimeModeFragment)
        }
        binding.rvModes.apply {
            adapter = timeModeAdapter
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.timeModeList.observe(viewLifecycleOwner) {
            timeModeAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimeModeClick(timeMode: TimeMode) {
        viewModel.saveTimeMode(timeMode)
        findNavController().navigate(R.id.action_settingsFragment_to_timersFragment)
    }
}