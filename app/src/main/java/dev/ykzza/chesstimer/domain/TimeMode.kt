package dev.ykzza.chesstimer.domain

data class TimeMode(
    val id: Int = UNDEFINED_ID,
    val baseTime: Long,
    val addTime: Long
){

    override fun toString(): String {
        return if(addTime > 0) {
            "${timeToString(baseTime)} | ${timeToString(addTime)}"
        } else {
            timeToString(baseTime)
        }
    }

    private fun timeToString(time: Long): String {
        val totalSeconds = time / MILLIS_IN_SECOND
        val minutes = totalSeconds / SECONDS_IN_MINUTE
        val seconds = totalSeconds % SECONDS_IN_MINUTE

        val minutePart = if (minutes > 0) "$minutes min " else ""
        val secondPart = if (seconds > 0) "$seconds sec" else ""

        return when {
            minutes > 0 && seconds > 0 -> "$minutePart$secondPart"
            minutes > 0 -> minutePart
            seconds > 0 -> secondPart
            else -> ""
        }
    }
    companion object {
        const val UNDEFINED_ID = 0

        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTE = 60L
    }
}
