package com.example.chessclock.clock


import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClockViewModel : ViewModel() {
    private  var START_TIME_IN_MILLIS: Long = 0L
    private var BONUS_TIME_IN_MILLIS: Long = 0L

    val player1Time = MutableLiveData<Long>()
    val player2Time = MutableLiveData<Long>()
    val timerStatus = MutableLiveData<Status>()

    private lateinit var countDownTimer: CountDownTimer

    init {
        player1Time.value = 0L
        player2Time.value = 0L
        timerStatus.value = Status.INIT
    }

    fun initialize(initialTime: Long, bonusTime: Long) {
        START_TIME_IN_MILLIS = initialTime
        BONUS_TIME_IN_MILLIS = bonusTime
        player1Time.value = START_TIME_IN_MILLIS
        player2Time.value = START_TIME_IN_MILLIS
    }

    fun startTimer(player: Player) {
        timerStatus.postValue(Status.RUNNING)
        val playerTimeInMills: Long

        if (player == Player.PLAYER1) {
            playerTimeInMills = player1Time.value!!
        } else {
           playerTimeInMills = player2Time.value!!
        }

        countDownTimer = object : CountDownTimer(playerTimeInMills, 1000) {
            override fun onTick(timeLeftInMills: Long) {
                if (player == Player.PLAYER1) {
                    player1Time.postValue(timeLeftInMills)
                } else {
                    player2Time.postValue(timeLeftInMills)
                }
            }

            override fun onFinish() {
                timerStatus.postValue(Status.FINISH)
            }
        }.start()
    }

    fun addBonusTime(player: Player) {
        if (player == Player.PLAYER1) {
            player1Time.postValue(player1Time.value!! + BONUS_TIME_IN_MILLIS)
        } else {
            player2Time.postValue(player2Time.value!! + BONUS_TIME_IN_MILLIS)
        }
    }

    fun switch() {
        countDownTimer.cancel()
    }

    fun pauseTimer() {
        countDownTimer.cancel()
        timerStatus.postValue(Status.PAUSED)
    }

    fun resetTimer() {
        countDownTimer.cancel()
        initialize(START_TIME_IN_MILLIS, BONUS_TIME_IN_MILLIS)
        timerStatus.postValue(Status.INIT)
    }
}