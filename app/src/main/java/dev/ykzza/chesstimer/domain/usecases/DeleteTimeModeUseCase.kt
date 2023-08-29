package dev.ykzza.chesstimer.domain.usecases

import dev.ykzza.chesstimer.domain.ChessTimerRepository

class DeleteTimeModeUseCase(
    private val repository: ChessTimerRepository
) {

    suspend operator fun invoke(id: Int) = repository.deleteTimeMode(id)
}