package dev.ykzza.chesstimer.domain

class AddTimeModeUseCase(
    private val repository: ChessTimerRepository
) {

    suspend operator fun invoke(timeMode: TimeMode) = repository.addTimeMode(timeMode)
}