package dev.ykzza.chesstimer.domain.usecases

import dev.ykzza.chesstimer.domain.ChessTimerRepository
import dev.ykzza.chesstimer.domain.TimeMode

class SaveTimeModeUseCase(
   private val repository: ChessTimerRepository
) {

    suspend operator fun invoke(timeMode: TimeMode) = repository.saveTimeMode(timeMode)
}