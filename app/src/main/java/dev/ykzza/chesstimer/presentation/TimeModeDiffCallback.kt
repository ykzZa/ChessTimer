package dev.ykzza.chesstimer.presentation

import androidx.recyclerview.widget.DiffUtil
import dev.ykzza.chesstimer.domain.TimeMode

class TimeModeDiffCallback: DiffUtil.ItemCallback<TimeMode>() {
    override fun areItemsTheSame(oldItem: TimeMode, newItem: TimeMode): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TimeMode, newItem: TimeMode): Boolean {
        return oldItem == newItem
    }
}