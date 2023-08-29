package dev.ykzza.chesstimer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.ykzza.chesstimer.R
import dev.ykzza.chesstimer.databinding.FragmentTimersBinding
import dev.ykzza.chesstimer.domain.Players

class TimersFragment : Fragment(), EditTimerDialog.OnAddClickListener {

    private var _binding: FragmentTimersBinding? = null
    private val binding: FragmentTimersBinding
        get() = _binding ?: throw RuntimeException("FragmentTimersBinding is null")

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setOnClickListeners()
        observeViewModel()
    }

    private fun setOnClickListeners() {
        binding.tvBlackTimer.setOnClickListener {
            it.isClickable = false
            binding.tvWhiteTimer.isClickable = true
            viewModel.timerClick(Players.BLACK)
        }
        binding.tvWhiteTimer.setOnClickListener {
            it.isClickable = false
            binding.tvBlackTimer.isClickable = true
            viewModel.timerClick(Players.WHITE)
        }
        binding.btnReset.setOnClickListener {
            viewModel.resetTimers()
            binding.btnPauseResume.visibility = View.VISIBLE
            resetTextViews()
        }
        binding.btnPauseResume.setOnClickListener {
            viewModel.playPauseClick()
        }
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_timersFragment_to_settingsFragment)
        }
        binding.ibSettingsWhite.setOnClickListener {
            val dialog = EditTimerDialog(this, Players.WHITE)
            dialog.show(childFragmentManager, "EditTimerDialog")
        }
        binding.ibSettingsBlack.setOnClickListener {
            val dialog = EditTimerDialog(this, Players.BLACK)
            dialog.show(childFragmentManager, "EditTimerDialog")
        }
    }

    private fun observeViewModel() {
        with(binding) {
            viewModel.blackFormattedTime.observe(viewLifecycleOwner) {
                tvBlackTimer.text = it
            }
            viewModel.whiteFormattedTime.observe(viewLifecycleOwner) {
                tvWhiteTimer.text = it
            }
            viewModel.blackLose.observe(viewLifecycleOwner) {
                tvBlackTimer.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.holo_red_light
                    )
                )
            }
            viewModel.whiteLose.observe(viewLifecycleOwner) {
                tvWhiteTimer.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.holo_red_light
                    )
                )
            }
            viewModel.oneTimerEnded.observe(viewLifecycleOwner) { timerEnded ->
                tvClickableReturn()
                if (timerEnded) {
                    btnPauseResume.visibility = View.INVISIBLE
                } else {
                    btnPauseResume.visibility = View.VISIBLE
                }
            }
            viewModel.activeTimers.observe(viewLifecycleOwner) { isActive ->
                updateImageButton(isActive)
                updateSettingImageButtons(isActive)
            }
            viewModel.screenClean.observe(viewLifecycleOwner) {
                resetTextViews()
            }
            viewModel.timeModeFormatted.observe(viewLifecycleOwner) {
                binding.timeModeBlack.text = it
                binding.timeModeWhite.text = it
            }
            viewModel.lockWhite.observe(viewLifecycleOwner) {
                tvWhiteTimer.isClickable = false
                tvBlackTimer.isClickable = true
            }
            viewModel.lockBlack.observe(viewLifecycleOwner) {
                tvBlackTimer.isClickable = false
                tvWhiteTimer.isClickable = true
            }
        }
    }

    private fun updateSettingImageButtons(isActive: Boolean) {
        if(!isActive) {
            binding.ibSettingsBlack.visibility = View.VISIBLE
            binding.ibSettingsWhite.visibility = View.VISIBLE
            binding.timeModeWhite.visibility = View.VISIBLE
            binding.timeModeBlack.visibility = View.VISIBLE
        } else {
            binding.ibSettingsBlack.visibility = View.INVISIBLE
            binding.ibSettingsWhite.visibility = View.INVISIBLE
            binding.timeModeWhite.visibility = View.INVISIBLE
            binding.timeModeBlack.visibility = View.INVISIBLE
        }
    }
    private fun updateImageButton(isActive: Boolean) {
        if(isActive) {
            binding.btnPauseResume.setImageResource(R.drawable.ic_pause)
        } else {
            binding.btnPauseResume.setImageResource(R.drawable.ic_play)
        }
    }
    private fun resetTextViews() {
        tvClickableReturn()
        binding.apply {
            tvBlackTimer.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            tvWhiteTimer.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        }
    }

    private fun tvClickableReturn() {
        binding.tvBlackTimer.isClickable = true
        binding.tvWhiteTimer.isClickable = true
    }
    override fun onAddClick(time: Long, players: Players) {
        viewModel.resetTimer(time, players)
    }
}