package com.example.chessclock.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.chessclock.R

class CustomTimeDialog : DialogFragment() {

    private lateinit var initialTimeSeekBar: SeekBar
    private lateinit var bonusTimeSeekBar: SeekBar
    private lateinit var initialTime: TextView
    private lateinit var bonusTime: TextView

    lateinit var listener: OnClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity?.layoutInflater!!
        val view = inflater.inflate(R.layout.layout_dialog, null)

        builder.setView(view)
            .setTitle("Custom Time")
            .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })
            .setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    var initialTime = 1
                    if (initialTimeSeekBar.progress != 0) {
                        initialTime = initialTimeSeekBar.progress
                    }
                    val bonusTime: Int = bonusTimeSeekBar.progress

                    listener.onClickListener(initialTime, bonusTime)
                }
            })

        initialTimeSeekBar = view.findViewById(R.id.initialTimeSeekbar)
        bonusTimeSeekBar = view.findViewById(R.id.bonusTimeSeekbar)
        initialTime = view.findViewById(R.id.initialTime)
        bonusTime = view.findViewById(R.id.bonusTime)

        initialTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var progress = 1
                if (p1 != 0) {
                    progress = p1
                }
                initialTime.text = "$progress min"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        bonusTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                bonusTime.text = "$p1 sec"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnClickListener
        } catch (e: ClassCastException) {
            Log.d("CustomTimeDialog", "onAttach: ${e.message}")
        }
    }

    interface OnClickListener {
        fun onClickListener(initialTime: Int, bonusTime: Int)
    }
}