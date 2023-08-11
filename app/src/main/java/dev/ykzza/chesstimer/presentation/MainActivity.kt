package dev.ykzza.chesstimer.presentation

import dev.ykzza.chesstimer.R

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import dev.ykzza.chesstimer.domain.Players
import dev.ykzza.chesstimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUi()
        setOnClickListeners()
        observeViewModel()
    }

    private fun setOnClickListeners() {
        binding.tvBlackTimer.setOnClickListener {
            viewModel.timerClick(Players.BLACK)
            it.isClickable = false
            binding.tvWhiteTimer.isClickable = true
        }
        binding.tvWhiteTimer.setOnClickListener {
            viewModel.timerClick(Players.WHITE)
            it.isClickable = false
            binding.tvBlackTimer.isClickable = true
        }
        binding.btnReset.setOnClickListener {
            viewModel.resetTimers()
            binding.btnPauseResume.visibility = View.VISIBLE
            resetTextViews()
        }
        binding.btnPauseResume.setOnClickListener {
            viewModel.playPauseClick()
        }
    }

    private fun observeViewModel() {
        with(binding) {
            viewModel.blackFormattedTime.observe(this@MainActivity) {
                tvBlackTimer.text = it
            }
            viewModel.whiteFormattedTime.observe(this@MainActivity) {
                tvWhiteTimer.text = it
            }
            viewModel.blackLose.observe(this@MainActivity) {
                tvBlackTimer.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        android.R.color.holo_red_light
                    )
                )
            }
            viewModel.whiteLose.observe(this@MainActivity) {
                tvWhiteTimer.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        android.R.color.holo_red_light
                    )
                )
            }
            viewModel.oneTimerEnded.observe(this@MainActivity) { timerEnded ->
                if (timerEnded) {
                    btnPauseResume.visibility = View.INVISIBLE
                }
            }
            viewModel.activeTimers.observe(this@MainActivity) { isActive ->
                updateImageButton(isActive)
                updateSettingImageButtons(isActive)
            }
        }
    }

    private fun updateSettingImageButtons(isActive: Boolean) {
        if(!isActive) {
            binding.ibSettingsBlack.visibility = View.VISIBLE
            binding.ibSettingsWhite.visibility = View.VISIBLE
        } else {
            binding.ibSettingsBlack.visibility = View.INVISIBLE
            binding.ibSettingsWhite.visibility = View.INVISIBLE
        }
    }
    private fun updateImageButton(isActive: Boolean) {
        if(isActive) {
            binding.btnPauseResume.setImageResource(R.drawable.baseline_pause)
        } else {
            binding.btnPauseResume.setImageResource(R.drawable.baseline_play_arrow)
        }
    }
    private fun resetTextViews() {
        binding.apply {
            tvBlackTimer.isClickable = true
            tvBlackTimer.setBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.white
                )
            )
            tvWhiteTimer.isClickable = true
            tvWhiteTimer.setBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.white
                )
            )
        }
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}


