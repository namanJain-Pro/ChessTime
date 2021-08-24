package com.example.chessclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.chessclock.dashboard.CustomTimeDialog
import com.example.chessclock.dashboard.DashboardFragmentDirections

class MainActivity : AppCompatActivity(), CustomTimeDialog.OnClickListener{

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onClickListener(initialTime: Int, bonusTime: Int) {
        val initialTimeInMills: Long = (initialTime * 60000).toLong()
        val bonusTimeInMills: Long = (bonusTime * 1000).toLong()
        Log.d("MainActivity", "onClickListener: $initialTime and $bonusTime")
        val action = DashboardFragmentDirections.actionDashboardFragmentToClockFragment(initialTimeInMills, bonusTimeInMills)
        navController.navigate(action)
    }
}