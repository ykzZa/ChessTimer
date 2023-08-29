package dev.ykzza.chesstimer.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.ykzza.chesstimer.R
import dev.ykzza.chesstimer.databinding.FragmentAddTimeModeBinding
import dev.ykzza.chesstimer.domain.TimeMode

class AddTimeModeFragment : Fragment() {

    private var _binding: FragmentAddTimeModeBinding? = null
    private val binding: FragmentAddTimeModeBinding
        get() = _binding ?: throw RuntimeException("FragmentAddTimeModeBinding is null")

    private val viewModel by lazy {
        ViewModelProvider(this)[AddTimeModeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTimeModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnTextChangedListeners()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnAdd.setOnClickListener {
            val timeMinutes = binding.etMinutes.text.toString()
            val timeSeconds = binding.etSeconds.text.toString()
            val timeMinIncrement = binding.etMinutesIncrement.text.toString()
            val timeSecIncrement = binding.etSecondsIncrement.text.toString()

            if (timeMinutes.isEmpty() && timeSeconds.isEmpty()) {
                throw RuntimeException("Fields are empty, button must be disabled")
            } else {
                val timeMode = TimeMode(
                    baseTime = stringTimeToLong(timeMinutes, timeSeconds),
                    addTime = stringTimeToLong(timeMinIncrement, timeSecIncrement)
                )
                viewModel.addTimeMode(timeMode)
                findNavController().navigate(R.id.action_addTimeModeFragment_to_settingsFragment)
            }
        }
        binding.ibBack.setOnClickListener {
            findNavController().navigate(R.id.action_addTimeModeFragment_to_settingsFragment)
        }
    }

    private fun setOnTextChangedListeners() {
        binding.etMinutes.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnAdd.isEnabled = !p0.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.etSeconds.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnAdd.isEnabled = !p0.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun stringTimeToLong(timeMinutes: String, timeSeconds: String): Long {
        var result = 0L
        if (timeMinutes.isNotEmpty()) {
            result += timeMinutes.toInt() * MILLIS_IN_MINUTE
        }
        if (timeSeconds.isNotEmpty()) {
            result += timeSeconds.toInt() * MILLIS_IN_SECOND
        }
        return result
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {

        private const val MILLIS_IN_SECOND = 1000L
        private const val MILLIS_IN_MINUTE = 60000L
    }
}