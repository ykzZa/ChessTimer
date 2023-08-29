package dev.ykzza.chesstimer.domain.usecases

import dev.ykzza.chesstimer.domain.ChessTimerRepository

class GetSavedTimeModeUseCase(
    private val repository: ChessTimerRepository
) {

    operator fun invoke() = repository.getSavedTimeMode()
}