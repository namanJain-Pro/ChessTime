package com.example.chessclock.clock

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chessclock.R
import com.example.chessclock.databinding.FragmentClockBinding
import java.util.*

class ClockFragment : Fragment(R.layout.fragment_clock) {

    private lateinit var binding: FragmentClockBinding
    private val args: ClockFragmentArgs by navArgs()
    private val viewModel: ClockViewModel by viewModels()

    private var timerStatus = Status.INIT
    private var toggle = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClockBinding.bind(view)

        val initialTime = args.initialTimeInMillisecond
        val bonusTime = args.bonusTimeInMillisecond
        viewModel.initialize(initialTime, bonusTime)

        binding.player1Timer.text = getFormattedTime(viewModel.player1Time.value!!)
        binding.player2Timer.text = getFormattedTime(viewModel.player2Time.value!!)

        binding.apply {

            player1.setOnClickListener {
                if (timerStatus == Status.RUNNING) {
                    if (toggle) {
                        toggle = false
                        viewModel.switch()
                        viewModel.startTimer(Player.PLAYER2)
                        if (bonusTime != 0L) viewModel.addBonusTime(Player.PLAYER1)
                    }
                } else {
                    if (timerStatus != Status.PAUSED) {
                        viewModel.startTimer(Player.PLAYER1)
                        timerStatus = Status.RUNNING
                        toggle = true
                    }
                }
                if (timerStatus != Status.PAUSED) {
                    switchColor()
                }
            }

            player2.setOnClickListener {
                if (timerStatus == Status.RUNNING && timerStatus != Status.PAUSED) {
                    if (!toggle) {
                        toggle = true
                        viewModel.switch()
                        viewModel.startTimer(Player.PLAYER1)
                        if (bonusTime != 0L) viewModel.addBonusTime(Player.PLAYER2)
                    }
                } else {
                    if (timerStatus != Status.PAUSED) {
                        viewModel.startTimer(Player.PLAYER2)
                        timerStatus = Status.RUNNING
                        toggle = false
                    }
                }
                if (timerStatus != Status.PAUSED) {
                    switchColor()
                }
            }

            pauseBtn.setOnClickListener {
                if (timerStatus == Status.RUNNING) {
                    binding.pauseBtn.setImageResource(R.drawable.resume)
                    viewModel.pauseTimer()
                } else {
                    binding.pauseBtn.setImageResource(R.drawable.pause)
                    if (toggle) {
                        viewModel.startTimer(Player.PLAYER1)
                    } else {
                        viewModel.startTimer(Player.PLAYER2)
                    }
                    switchColor()
                }
            }

            resetBtn.setOnClickListener {
                if (timerStatus != Status.INIT) {
                    viewModel.resetTimer()
                }
            }

            homeBtn.setOnClickListener {
                findNavController().navigate(R.id.action_clockFragment_to_dashboardFragment)
            }
        }

        viewModel.player1Time.observe(viewLifecycleOwner, {
            val formattedTime = getFormattedTime(viewModel.player1Time.value!!)
            binding.player1Timer.text = formattedTime
            binding.sideClockPLayer1.text = formattedTime
        })

        viewModel.player2Time.observe(viewLifecycleOwner, {
            val formattedTime = getFormattedTime(viewModel.player2Time.value!!)
            binding.player2Timer.text = formattedTime
            binding.sideClockPLayer2.text = formattedTime
        })

        viewModel.timerStatus.observe(viewLifecycleOwner, {
            timerStatus = viewModel.timerStatus.value!!
            when (viewModel.timerStatus.value) {
                Status.RUNNING -> {
                    binding.timeoutText.visibility = View.GONE
                    binding.timerLotti.visibility = View.VISIBLE
                }

                Status.PAUSED -> {
                    resetColor()
                    binding.timeoutText.text = "Paused"
                    binding.timeoutText.visibility = View.VISIBLE
                    binding.timerLotti.visibility = View.GONE
                }

                Status.FINISH -> {
                    resetColor()
                    binding.timeoutText.text = "Time Out: If you wanna continue reset the clock"
                    binding.timeoutText.visibility = View.VISIBLE
                    binding.timerLotti.visibility = View.GONE
                }

                Status.INIT -> {
                    resetColor()
                    binding.timeoutText.text = "All the best"
                    binding.timeoutText.visibility = View.VISIBLE
                    binding.timerLotti.visibility = View.GONE
                }
            }
        })
    }

    private fun switchColor() {
        if (toggle) {
            binding.player1.setBackgroundColor(Color.parseColor("#3870c9"))
            binding.sideClockPLayer1.setTextColor(Color.parseColor("#3870c9"))
            binding.player2.setBackgroundColor(Color.parseColor("#363636"))
            binding.sideClockPLayer2.setTextColor(Color.parseColor("#ffffff"))
        } else {
            binding.player1.setBackgroundColor(Color.parseColor("#363636"))
            binding.sideClockPLayer1.setTextColor(Color.parseColor("#ffffff"))
            binding.player2.setBackgroundColor(Color.parseColor("#3870c9"))
            binding.sideClockPLayer2.setTextColor(Color.parseColor("#3870c9"))
        }
    }

    private fun resetColor() {
        binding.player1.setBackgroundColor(Color.parseColor("#363636"))
        binding.player2.setBackgroundColor(Color.parseColor("#363636"))
        binding.sideClockPLayer1.setTextColor(Color.parseColor("#ffffff"))
        binding.sideClockPLayer2.setTextColor(Color.parseColor("#ffffff"))
    }

    private fun getFormattedTime(timeLeft: Long): String {
        val minutes = ((timeLeft / 1000) / 60).toInt()
        val seconds = ((timeLeft / 1000) % 60).toInt()

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

}