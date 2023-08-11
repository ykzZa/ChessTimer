package dev.ykzza.chesstimer.domain

class GetTimeModeListUseCase(
    private val repository: ChessTimerRepository
){

    operator fun invoke() = repository.getTimeModeList()
}