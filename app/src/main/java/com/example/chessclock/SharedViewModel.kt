package com.example.chessclock

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val SHARED_PREFERENCES = "user-preferences"
const val CUSTOM_TIME = "custom-time"

class SharedViewModel(application: Application) : AndroidViewModel(application) {


    private val context = application.applicationContext

    val customTimeList = MutableLiveData<List<String>>()

    init {
        customTimeList.value = listOf()
    }

    fun addCustomTimeList(initialTimeInMills: Long, bonusTimeInMills: Long) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(CUSTOM_TIME, null)

        with(sharedPreferences.edit()){
            if (value == null) {
                val str = "$initialTimeInMills,$bonusTimeInMills"
                putString(CUSTOM_TIME, str)
                apply()
                customTimeList.value = listOf(str)
            } else {
                val tempList = value.split("-") as ArrayList
                if (tempList.size < 3) {
                    tempList.add("$initialTimeInMills,$bonusTimeInMills")
                } else {
                    val str1 = tempList[tempList.size-2]
                    val str2 = tempList[tempList.size-1]

                    tempList.clear()
                    tempList.add(str1)
                    tempList.add(str2)
                    tempList.add("$initialTimeInMills,$bonusTimeInMills")
                }
                customTimeList.postValue(tempList)
                val updatedPref = tempList.joinToString("-")
                putString(CUSTOM_TIME, updatedPref)
                apply()
            }
        }
    }

    fun loadPreferences() {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(CUSTOM_TIME, null)
        Log.d("SharedViewModel", "loadPreferences: $value")
        if (value != null) {
           val tempList = value.split("-")
           customTimeList.postValue(tempList)
        }
    }
}