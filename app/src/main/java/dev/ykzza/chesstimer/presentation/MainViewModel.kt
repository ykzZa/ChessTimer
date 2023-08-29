package dev.ykzza.chesstimer.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.orbitalsonic.sonictimer.SonicCountDownTimer
import dev.ykzza.chesstimer.data.ChessTimerRepositoryImpl
import dev.ykzza.chesstimer.domain.Players
import dev.ykzza.chesstimer.domain.TimeContainer
import dev.ykzza.chesstimer.domain.TimeMode
import dev.ykzza.chesstimer.domain.usecases.GetSavedTimeModeUseCase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChessTimerRepositoryImpl(application)
    private val getSavedTimeModeUseCase = GetSavedTimeModeUseCase(repository)

    private var savedTimeMode = getSavedTimeModeUseCase().asFlow()
    private lateinit var timeMode: TimeMode

    private lateinit var blackChessTimer: SonicCountDownTimer
    private lateinit var whiteChessTimer: SonicCountDownTimer

    private var blackChessTime = TimeContainer()
    private var whiteChessTime = TimeContainer()

    private var moveTurn: Players? = null

    private var _timeModeFormatted = MutableLiveData<String>()
    val timeModeFormatted: LiveData<String>
        get() = _timeModeFormatted

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

    private val _screenClean = MutableLiveData<Unit>()
    val screenClean: LiveData<Unit>
        get() = _screenClean

    private val _lockBlack = MutableLiveData<Unit>()
    val lockBlack: LiveData<Unit>
        get() = _lockBlack

    private val _lockWhite = MutableLiveData<Unit>()
    val lockWhite: LiveData<Unit>
        get() = _lockWhite

    init {
        viewModelScope.launch {
            savedTimeMode.collect {
                timeMode = it
                _timeModeFormatted.value = timeMode.toString()
                resetTimers()
            }
        }
        _activeTimers.value = false
    }

    fun timerClick(clickedBy: Players) {
        if (_oneTimerEnded.value == false) {
            if (_activeTimers.value == false) {
                if (moveTurn == null) {
                    pauseCurrentStartOpposite(clickedBy)
                    changeMoveTurn(clickedBy)
                    _activeTimers.value = true
                } else {
                    playPauseClick()
                }
            } else {
                pauseCurrentStartOpposite(clickedBy)
                addTimeToTimer(clickedBy)
                changeMoveTurn(clickedBy)
                _activeTimers.value = true
            }
        } else {
            resetTimers()
            _screenClean.value = Unit
            _activeTimers.value = false
        }
    }

    fun resetTimers(
        whiteTime: Long = timeMode.baseTime,
        blackTime: Long = timeMode.baseTime
    ) {
        _whiteFormattedTime.value = formatTime(whiteTime)
        _blackFormattedTime.value = formatTime(blackTime)

        blackChessTime.time = blackTime
        whiteChessTime.time = whiteTime

        _oneTimerEnded.value = false

        pauseTimers()
        cancelTimers()
        initTimers(whiteTime, blackTime)

        _activeTimers.value = false
        moveTurn = null
    }

    fun resetTimer(time: Long, timerColor: Players) {
        when (timerColor) {
            Players.BLACK -> {
                _blackFormattedTime.value = formatTime(time)
                blackChessTime.time = time
                blackChessTimer.cancelCountDownTimer()
                blackChessTimer = initTimer(time, _blackFormattedTime, _blackLose, blackChessTime)
            }

            Players.WHITE -> {
                _whiteFormattedTime.value = formatTime(time)
                whiteChessTime.time = time
                whiteChessTimer.cancelCountDownTimer()
                whiteChessTimer = initTimer(time, _whiteFormattedTime, _whiteLose, whiteChessTime)
            }
        }
    }

    fun playPauseClick() {
        if (moveTurn == null) {
            changeMoveTurn(Players.BLACK)
            blackChessTimer.resumeCountDownTimer()
            _lockWhite.value = Unit
            _activeTimers.value = true
        } else {
            if (_activeTimers.value == true) {
                pauseTimers()
                _activeTimers.value = false
            } else {
                _activeTimers.value = true
                when (moveTurn) {
                    Players.BLACK -> {
                        _lockWhite.value = Unit
                        whiteChessTimer.pauseCountDownTimer()
                        blackChessTimer.resumeCountDownTimer()
                    }

                    Players.WHITE -> {
                        _lockBlack.value = Unit
                        blackChessTimer.pauseCountDownTimer()
                        whiteChessTimer.resumeCountDownTimer()
                    }

                    null -> throw RuntimeException()
                }
            }
        }
    }

    private fun addTimeToTimer(clickedBy: Players) {
        when (clickedBy) {
            Players.BLACK -> {
                blackChessTime.time += timeMode.addTime
                resetTimer(blackChessTime.time, clickedBy)
            }

            Players.WHITE -> {
                whiteChessTime.time += timeMode.addTime
                resetTimer(whiteChessTime.time, clickedBy)
            }
        }
    }

    private fun cancelTimers() {
        if (this::blackChessTimer.isInitialized && this::whiteChessTimer.isInitialized) {
            whiteChessTimer.cancelCountDownTimer()
            blackChessTimer.cancelCountDownTimer()
        }

    }

    private fun pauseTimers() {
        if (this::blackChessTimer.isInitialized && this::whiteChessTimer.isInitialized) {
            blackChessTimer.pauseCountDownTimer()
            whiteChessTimer.pauseCountDownTimer()
        }
    }

    private fun changeMoveTurn(clickedBy: Players) {
        moveTurn = when (clickedBy) {
            Players.BLACK -> Players.WHITE
            Players.WHITE -> Players.BLACK
        }
    }

    private fun pauseCurrentStartOpposite(clickedBy: Players) {
        when (clickedBy) {
            Players.BLACK -> {
                whiteChessTimer.resumeCountDownTimer()
                blackChessTimer.pauseCountDownTimer()
            }

            Players.WHITE -> {
                blackChessTimer.resumeCountDownTimer()
                whiteChessTimer.pauseCountDownTimer()
            }
        }
    }

    private fun initTimers(whiteTime: Long, blackTime: Long) {
        whiteChessTimer = initTimer(whiteTime, _whiteFormattedTime, _whiteLose, whiteChessTime)
        blackChessTimer = initTimer(blackTime, _blackFormattedTime, _blackLose, blackChessTime)
    }

    private fun initTimer(
        time: Long,
        formattedTime: MutableLiveData<String>,
        timerFinished: MutableLiveData<Unit>,
        timeHolder: TimeContainer
    ): SonicCountDownTimer {
        val timer = object : SonicCountDownTimer(time, COUNT_TIME) {
            override fun onTimerFinish() {
                _oneTimerEnded.value = true
                timerFinished.value = Unit
                _activeTimers.value = false
                moveTurn = null
            }

            override fun onTimerTick(timeRemaining: Long) {
                formattedTime.value = formatTime(timeRemaining)
                timeHolder.time = timeRemaining
            }
        }
        return timer
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

        private const val COUNT_TIME = 100L
    }
}