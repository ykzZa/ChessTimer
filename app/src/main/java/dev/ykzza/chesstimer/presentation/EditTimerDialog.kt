package dev.ykzza.chesstimer.presentation

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dev.ykzza.chesstimer.databinding.EditTimerDialogBinding
import dev.ykzza.chesstimer.domain.Players

class EditTimerDialog(
    private val onAddClickListener: OnAddClickListener,
    private val players: Players
) : DialogFragment() {

    private var _binding: EditTimerDialogBinding? = null
    private val binding: EditTimerDialogBinding
        get() = _binding ?: throw RuntimeException("EditTimerDialogBinding is null")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            _binding = EditTimerDialogBinding.inflate(layoutInflater)

            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add time mode")
            builder.setView(binding.root)
                .setPositiveButton(
                    "Add"
                ) { _, _ ->
                    val minutes = binding.etMinutes.text.toString()
                    val seconds = binding.etSeconds.text.toString()
                    Log.d("whiteTime", "$minutes:$seconds")
                    onAddClickListener.onAddClick(stringTimeToLong(minutes, seconds), players)
                }
                .setNegativeButton(
                    "Cancel"
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun stringTimeToLong(timeMinutes: String, timeSeconds: String): Long {
        var result = 0L
        if (timeMinutes.isNotEmpty()) {
            result += timeMinutes.toInt() * MILLIS_IN_MINUTE
        }
        if (timeSeconds.isNotEmpty()) {
            result += timeSeconds.toInt() * MILLIS_IN_SECOND
        }
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface OnAddClickListener {
        fun onAddClick(time: Long, players: Players)
    }

    companion object {

        private const val MILLIS_IN_SECOND = 1000L
        private const val MILLIS_IN_MINUTE = 60000L
    }
}