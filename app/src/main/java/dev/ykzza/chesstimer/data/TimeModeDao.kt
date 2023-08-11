package dev.ykzza.chesstimer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeModeDao {

    @Query("SELECT * FROM time_modes WHERE id = :id LIMIT 1")
    suspend fun getTimeModeById(id: Int): TimeModeDbModel

    @Query("DELETE FROM time_modes WHERE id = :id")
    suspend fun deleteTimeMode(id: Int)

    @Insert
    suspend fun addTimeMode(timeMode: TimeModeDbModel)

    @Query("SELECT * FROM time_modes")
    fun getTimeModeList(): LiveData<List<TimeModeDbModel>>
}