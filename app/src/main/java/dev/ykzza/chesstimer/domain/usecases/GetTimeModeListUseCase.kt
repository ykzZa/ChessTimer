package dev.ykzza.chesstimer.domain.usecases

import dev.ykzza.chesstimer.domain.ChessTimerRepository

class GetTimeModeListUseCase(
    private val repository: ChessTimerRepository
){

    operator fun invoke() = repository.getTimeModeList()
}