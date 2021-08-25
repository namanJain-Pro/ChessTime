package com.example.chessclock

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val SHARED_PREFERENCES = "user-preferences"
    private val CUSTOM_TIME = "custom-time"

    val customTimeList = MutableLiveData<List<String>>()

    init {
        customTimeList.value = listOf()
    }

    fun addCustomTimeList(context: Context, initialTimeInMills: Long, bonusTimeInMills: Long) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(CUSTOM_TIME, null)

        with(sharedPreferences.edit()){
            if (value == null) {
                val str = "$initialTimeInMills,$bonusTimeInMills"
                putString(CUSTOM_TIME, str)
                customTimeList.postValue(listOf(str))
            } else {
                val tempList = value.split("-") as ArrayList
                var updatedPref = ""
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
                updatedPref = tempList.joinToString("-")
                putString(CUSTOM_TIME, updatedPref)
            }
        }
    }

    fun loadPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(CUSTOM_TIME, null)
        Log.d("SharedViewModel", "loadPreferences: $value")
        if (value != null) {
           val tempList = value.split("-")
           customTimeList.postValue(tempList)
        }
    }

}