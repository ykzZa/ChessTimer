package dev.ykzza.chesstimer.domain

import androidx.lifecycle.LiveData

interface ChessTimerRepository {

    fun getSavedTimeMode(): LiveData<TimeMode>

    suspend fun saveTimeMode(timeMode: TimeMode)

    suspend fun deleteTimeMode(id: Int)

    suspend fun addTimeMode(timeMode: TimeMode)

    fun getTimeModeList(): LiveData<List<TimeMode>>
}