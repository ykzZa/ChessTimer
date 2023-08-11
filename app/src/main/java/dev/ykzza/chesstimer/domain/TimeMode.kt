package dev.ykzza.chesstimer.domain

data class TimeMode(
    val id: Int = UNDEFINED_ID,
    val baseTime: Long,
    val addTime: Long
){

    companion object {
        const val UNDEFINED_ID = 0
    }
}
