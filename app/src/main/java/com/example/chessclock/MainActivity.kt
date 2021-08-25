package com.example.chessclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.chessclock.dashboard.CustomTimeDialog
import com.example.chessclock.dashboard.DashboardFragmentDirections
import hotchemi.android.rate.AppRate

class MainActivity : AppCompatActivity(), CustomTimeDialog.OnClickListener{

    private lateinit var navController: NavController
    private val viewModel: SharedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // This is to get rating from user
        AppRate.with(this)
            .setInstallDays(1)
            .setLaunchTimes(3)
            .setRemindInterval(2)
            .monitor()

        AppRate.showRateDialogIfMeetsConditions(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onClickListener(initialTime: Int, bonusTime: Int) {
        val initialTimeInMills: Long = (initialTime * 60000).toLong()
        val bonusTimeInMills: Long = (bonusTime * 1000).toLong()

        viewModel.addCustomTimeList(applicationContext, initialTimeInMills, bonusTimeInMills)

        val action = DashboardFragmentDirections.actionDashboardFragmentToClockFragment(initialTimeInMills, bonusTimeInMills)
        navController.navigate(action)
    }
}