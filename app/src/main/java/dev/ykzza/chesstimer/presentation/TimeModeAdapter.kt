package dev.ykzza.chesstimer.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ykzza.chesstimer.databinding.TimeModeRowLayoutBinding
import dev.ykzza.chesstimer.domain.TimeMode

class TimeModeAdapter(private val onTimeModeClickListener: OnTimeModeClickListener) :
    ListAdapter<TimeMode, TimeModeAdapter.TimeModeViewHolder>(TimeModeDiffCallback()) {

    inner class TimeModeViewHolder(private val binding: TimeModeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(timeMode: TimeMode) {
            binding.apply {
                tvTimeMode.text = "$timeMode"
                root.setOnClickListener {
                    onTimeModeClickListener.onTimeModeClick(timeMode)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeModeViewHolder {
        val binding =
            TimeModeRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeModeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeModeViewHolder, position: Int) {
        val timeMode = getItem(position)
        holder.bind(timeMode)
    }

    interface OnTimeModeClickListener {
        fun onTimeModeClick(timeMode: TimeMode)
    }
}