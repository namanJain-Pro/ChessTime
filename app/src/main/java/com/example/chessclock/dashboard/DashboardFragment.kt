package com.example.chessclock.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chessclock.R
import com.example.chessclock.SharedViewModel
import com.example.chessclock.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDashboardBinding.bind(view)

        val viewModel: SharedViewModel by viewModels()
        viewModel.loadPreferences()

        binding.apply {

            //Bullet
            bulletBtn1.setOnClickListener { onButtonClicked(60000L, 0L) }
            bulletBtn2.setOnClickListener { onButtonClicked(60000L, 1000L) }
            bulletBtn3.setOnClickListener { onButtonClicked(120000L, 1000L) }

            blitzBtn1.setOnClickListener { onButtonClicked(180000L, 0L) }
            blitzBtn2.setOnClickListener { onButtonClicked(180000L, 2000L) }
            blitzBtn3.setOnClickListener { onButtonClicked(300000L, 0L) }
            blitzBtn4.setOnClickListener { onButtonClicked(300000L, 5000L) }

            rapidBtn1.setOnClickListener { onButtonClicked(600000L, 0L) }
            rapidBtn2.setOnClickListener { onButtonClicked(1800000L, 0L) }
            rapidBtn3.setOnClickListener { onButtonClicked(900000L, 10000L) }
            rapidBtn4.setOnClickListener { onButtonClicked(1200000L, 0L) }
            rapidBtn5.setOnClickListener { onButtonClicked(3600000L, 0L) }
            rapidBtn6.setOnClickListener { onButtonClicked(2700000L, 45000L) }

            customTime.setOnClickListener {
                val customTimeDialog = CustomTimeDialog()
                customTimeDialog.show(childFragmentManager, "Custom Time")
            }
        }

        viewModel.customTimeList.observe(viewLifecycleOwner, {
            Log.d("DashboardFragment", "onViewCreated: ${viewModel.customTimeList.value.toString()}")
        })
    }

    private fun onButtonClicked(initialTime: Long, bonusTime: Long) {
        val action = DashboardFragmentDirections.actionDashboardFragmentToClockFragment(initialTime, bonusTime)
        findNavController().navigate(action)
    }


}