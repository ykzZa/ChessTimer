package dev.ykzza.chesstimer.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.ykzza.chesstimer.data.ChessTimerRepositoryImpl
import dev.ykzza.chesstimer.domain.usecases.AddTimeModeUseCase
import dev.ykzza.chesstimer.domain.TimeMode
import kotlinx.coroutines.launch

class AddTimeModeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChessTimerRepositoryImpl(application)

    private val addTimeModeUseCase = AddTimeModeUseCase(repository)

    fun addTimeMode(timeMode: TimeMode) {
        viewModelScope.launch {
            addTimeModeUseCase(timeMode)
        }
    }
}