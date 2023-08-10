package dev.ykzza.chesstimer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orbitalsonic.sonictimer.SonicCountDownTimer
import dev.ykzza.chesstimer.domain.Players
import java.lang.RuntimeException

class MainViewModel: ViewModel() {

    private var blackChessTimer: SonicCountDownTimer? = null
    private var whiteChessTimer: SonicCountDownTimer? = null

    private var moveTurn: Players? = null

    private var _activeTimers = MutableLiveData<Boolean>()
    val activeTimers: LiveData<Boolean>
        get() = _activeTimers

    private var _oneTimerEnded = MutableLiveData<Boolean>()
    val oneTimerEnded: LiveData<Boolean>
        get() = _oneTimerEnded

    private val _blackLose = MutableLiveData<Unit>()
    val blackLose: LiveData<Unit>
        get() = _blackLose

    private val _whiteLose = MutableLiveData<Unit>()
    val whiteLose: LiveData<Unit>
        get() = _whiteLose

    private val _blackFormattedTime = MutableLiveData<String>()
    val blackFormattedTime: LiveData<String>
        get() = _blackFormattedTime

    private val _whiteFormattedTime = MutableLiveData<String>()
    val whiteFormattedTime: LiveData<String>
        get() = _whiteFormattedTime

    init {
        _activeTimers.value = false
        _whiteFormattedTime.value = formatTime(WHITE_CHESS_TIME)
        _blackFormattedTime.value = formatTime(BLACK_CHESS_TIME)
        resetTimers()
    }

    fun resetTimers() {
        _oneTimerEnded.value = false
        whiteChessTimer?.cancelCountDownTimer()
        whiteChessTimer = null
        blackChessTimer?.cancelCountDownTimer()
        blackChessTimer = null
        _whiteFormattedTime.value = formatTime(WHITE_CHESS_TIME)
        _blackFormattedTime.value = formatTime(BLACK_CHESS_TIME)
        initTimers()
        _activeTimers.value = false
        moveTurn = null
    }

    fun timerClick(clickedBy: Players) {
        if(_oneTimerEnded.value == false) {
            pauseCurrentStartOpposite(clickedBy)
            changeMoveTurn(clickedBy)
            _activeTimers.value = true
        } else {
            resetTimers()
        }
    }

    fun playPauseClick() {
        if(moveTurn == null) {
            timerClick(Players.BLACK)
        } else {
            if(_activeTimers.value == true) {
                pauseTimers()
                _activeTimers.value = false
            } else {
                when(moveTurn) {
                    Players.BLACK -> timerClick(Players.WHITE)
                    Players.WHITE -> timerClick(Players.BLACK)
                    null -> throw RuntimeException()
                }
            }
        }
    }

    private fun pauseTimers() {
        blackChessTimer?.pauseCountDownTimer()
        whiteChessTimer?.pauseCountDownTimer()
    }

    private fun changeMoveTurn(clickedBy: Players) {
        moveTurn = when(clickedBy) {
            Players.BLACK -> Players.WHITE
            Players.WHITE -> Players.BLACK
        }
    }

    private fun pauseCurrentStartOpposite(clickedBy: Players) {
        when(clickedBy) {
            Players.BLACK -> {
                whiteChessTimer?.resumeCountDownTimer()
                blackChessTimer?.pauseCountDownTimer()
            }
            Players.WHITE -> {
                blackChessTimer?.resumeCountDownTimer()
                whiteChessTimer?.pauseCountDownTimer()
            }
        }
    }
    private fun initTimers() {
        whiteChessTimer = object : SonicCountDownTimer(WHITE_CHESS_TIME, COUNT_TIME) {

            override fun onTimerFinish() {
                _whiteLose.value = Unit
                _oneTimerEnded.value = true
            }

            override fun onTimerTick(timeRemaining: Long) {
                _whiteFormattedTime.value = formatTime(timeRemaining)
            }
        }
        blackChessTimer = object : SonicCountDownTimer(BLACK_CHESS_TIME, COUNT_TIME) {

            override fun onTimerFinish() {
                _oneTimerEnded.value = true
                _blackLose.value = Unit
            }

            override fun onTimerTick(timeRemaining: Long) {
                _blackFormattedTime.value = formatTime(timeRemaining)
            }
        }
    }

    private fun formatTime(timeMillis: Long): String {
        val seconds = timeMillis / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - minutes * SECONDS_IN_MINUTES
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60

        private const val BLACK_CHESS_TIME = 10000L
        private const val WHITE_CHESS_TIME = 10000L

        private const val COUNT_TIME = 10L
    }
}