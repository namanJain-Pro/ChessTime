package com.example.chessclock

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chessclock.databinding.FragmentClockBinding
import java.util.*

enum class Player {
    PLAYER1, PLAYER2
}

class ClockFragment : Fragment(R.layout.fragment_clock) {

    companion object {
        private const val TAG = "ClockFragment"
    }
    private lateinit var binding: FragmentClockBinding
    private val args: ClockFragmentArgs by navArgs()

    // *********************************** \\
    private lateinit var countDownTimer: CountDownTimer
    private var timerStarted = false
    private var timerRunning = false
    private var player1Timer: Long = 0L
    private var player2Timer: Long = 0L
    private var toggle = false
    private lateinit var currentPlayer: Player
    private var bonusTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClockBinding.bind(view)

        val initialTime = args.initialTimeInMillisecond
        bonusTime = args.bonusTimeInMillisecond

        initialize(initialTime)

        binding.apply {

            player1.setOnClickListener {
                if (timerStarted) {
                    if (toggle) {
                        pauseTimer()
                        toggle = false
                        startTimer(Player.PLAYER2)
                    }

                } else {
                    startTimer(Player.PLAYER1)
                    toggle = true
                    timerStarted = true
                    statusVisibility(true)
                }
            }

            player2.setOnClickListener {
                if (timerStarted) {
                    if (!toggle) {
                        pauseTimer()
                        toggle = true
                        startTimer(Player.PLAYER1)
                    }

                } else {
                    startTimer(Player.PLAYER2)
                    toggle = false
                    timerStarted = true
                    statusVisibility(true)
                }
            }

            pauseBtn.setOnClickListener {
                if (timerStarted) {
                    if (timerRunning) {
                        pauseBtn.setImageResource(R.drawable.resume)
                        countDownTimer.cancel()
                        timerRunning = false
                        binding.player1.isClickable = false
                        binding.player2.isClickable = false
                        statusVisibility(false)
                        binding.timeoutText.text = "Paused"
                    } else {
                        pauseBtn.setImageResource(R.drawable.pause)
                        startTimer(currentPlayer)
                        timerRunning = true
                        binding.player1.isClickable = false
                        binding.player2.isClickable = false
                        statusVisibility(true)
                    }
                }
            }

            resetBtn.setOnClickListener {
                resetTimer(initialTime)
            }

            homeBtn.setOnClickListener {
                findNavController().navigate(R.id.action_clockFragment_to_dashboardFragment)
            }

        }

    }

    private fun initialize(initialTime: Long) {
        binding.apply {
            player1Timer.text = getTimeFormatted(initialTime)
            player2Timer.text = getTimeFormatted(initialTime)
        }
        binding.player1.setBackgroundColor(Color.parseColor("#363636"))
        binding.player2.setBackgroundColor(Color.parseColor("#363636"))
        binding.timeoutText.text = "All the best"
        player1Timer = initialTime
        player2Timer = initialTime
    }

    private fun startTimer(player: Player) {
        currentPlayer = player
        var timeLeft: Long
        if (player == Player.PLAYER1) {
            binding.player1.setBackgroundColor(Color.parseColor("#3870c9"))
            binding.player2.setBackgroundColor(Color.parseColor("#363636"))
            timeLeft = player1Timer
        } else {
            binding.player1.setBackgroundColor(Color.parseColor("#363636"))
            binding.player2.setBackgroundColor(Color.parseColor("#3870c9"))
            timeLeft = player2Timer
        }

        countDownTimer = object : CountDownTimer(timeLeft, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                if (player == Player.PLAYER1)
                    player1Timer = timeLeft
                else
                    player2Timer = timeLeft
                updateCountDownText(player, timeLeft)
            }

            override fun onFinish() {
                binding.player1.isClickable = false
                binding.player2.isClickable = false
                statusVisibility(false)
                binding.timeoutText.text = "Time Out: If you wanna continue reset the clock"
            }
        }.start()
        timerRunning = true
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        timerRunning = false
    }

    private fun resetTimer(initialTime: Long) {
        initialize(initialTime)
        countDownTimer.cancel()
        timerStarted = false
        timerRunning = false
    }

    private fun updateCountDownText(player: Player, playerTimer: Long) {
        if (player == Player.PLAYER1) {
            binding.player1Timer.text = getTimeFormatted(playerTimer)
        } else {
            binding.player2Timer.text = getTimeFormatted(playerTimer)
        }
    }

    private fun getTimeFormatted(timeInMilli: Long): String {
        val min: Int = ((timeInMilli / 1000) / 60).toInt()
        val sec: Int = ((timeInMilli / 1000) % 60).toInt()

        return String.format(Locale.getDefault(), "%02d:%02d", min, sec)
    }

    private fun statusVisibility(status: Boolean) {
        if (status) {
            binding.timeoutText.visibility = View.GONE
            binding.timerLotti.visibility = View.VISIBLE
        } else {
            binding.timeoutText.visibility = View.VISIBLE
            binding.timerLotti.visibility = View.GONE
        }
    }

}