package dev.ykzza.chesstimer.domain

import androidx.lifecycle.LiveData

interface ChessTimerRepository {

    suspend fun getTimeModeById(id: Int): TimeMode

    suspend fun deleteTimeMode(id: Int)

    suspend fun addTimeMode(timeMode: TimeMode)

    fun getTimeModeList(): LiveData<List<TimeMode>>
}