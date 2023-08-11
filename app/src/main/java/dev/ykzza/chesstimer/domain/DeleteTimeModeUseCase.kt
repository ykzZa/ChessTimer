package dev.ykzza.chesstimer.domain

class DeleteTimeModeUseCase(
    private val repository: ChessTimerRepository
) {

    suspend operator fun invoke(id: Int) = repository.deleteTimeMode(id)
}