package dev.ykzza.chesstimer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("time_modes")
data class TimeModeDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val baseTime: Long,
    val addTime: Long
)