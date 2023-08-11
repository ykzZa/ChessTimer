package dev.ykzza.chesstimer.domain

class GetTimeModeUseCase(
    private val repository: ChessTimerRepository
) {

    suspend operator fun invoke(id: Int) = repository.getTimeModeById(id)
}