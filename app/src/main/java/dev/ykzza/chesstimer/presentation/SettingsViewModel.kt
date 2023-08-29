package dev.ykzza.chesstimer.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.ykzza.chesstimer.data.ChessTimerRepositoryImpl
import dev.ykzza.chesstimer.domain.TimeMode
import dev.ykzza.chesstimer.domain.usecases.GetTimeModeListUseCase
import dev.ykzza.chesstimer.domain.usecases.SaveTimeModeUseCase
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ChessTimerRepositoryImpl(application)

    private val getTimeModeListUseCase = GetTimeModeListUseCase(repository)
    private val saveTimeModeUseCase = SaveTimeModeUseCase(repository)

    val timeModeList = getTimeModeListUseCase()

    fun saveTimeMode(timeMode: TimeMode) {
        viewModelScope.launch {
            saveTimeModeUseCase(timeMode)
        }
    }
}