package com.example.chessclock.dashboard

import android.os.Bundle
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
            binding.apply {
                if (viewModel.customTimeList.value?.isNotEmpty()!!) {
                    customLayout.visibility = View.VISIBLE
                }
                val initialTimeInMilli = arrayListOf<Long>()
                val bonusTimeInMilli = arrayListOf<Long>()

                for (i in it.indices.reversed()) {
                    val tmpList = it[i].split(",")
                    initialTimeInMilli.add(tmpList[0].toLong())
                    bonusTimeInMilli.add(tmpList[1].toLong())
                }

                when (initialTimeInMilli.size) {
                    1 -> {
                        customBtn1.visibility = View.VISIBLE
                        customBtn1.text = getFormattedText(initialTimeInMilli[0], bonusTimeInMilli[0])
                        customBtn1.setOnClickListener{onButtonClicked(initialTimeInMilli[0], bonusTimeInMilli[0])}
                    }

                    2 -> {
                        customBtn1.visibility = View.VISIBLE
                        customBtn1.text = getFormattedText(initialTimeInMilli[0], bonusTimeInMilli[0])
                        customBtn1.setOnClickListener{onButtonClicked(initialTimeInMilli[0], bonusTimeInMilli[0])}
                        customBtn2.visibility = View.VISIBLE
                        customBtn2.text = getFormattedText(initialTimeInMilli[1], bonusTimeInMilli[1])
                        customBtn2.setOnClickListener{onButtonClicked(initialTimeInMilli[1], bonusTimeInMilli[1])}
                    }

                    3 -> {
                        customBtn1.visibility = View.VISIBLE
                        customBtn1.text = getFormattedText(initialTimeInMilli[0], bonusTimeInMilli[0])
                        customBtn1.setOnClickListener{onButtonClicked(initialTimeInMilli[0], bonusTimeInMilli[0])}
                        customBtn2.visibility = View.VISIBLE
                        customBtn2.text = getFormattedText(initialTimeInMilli[1], bonusTimeInMilli[1])
                        customBtn2.setOnClickListener{onButtonClicked(initialTimeInMilli[1], bonusTimeInMilli[1])}
                        customBtn3.visibility = View.VISIBLE
                        customBtn3.text = getFormattedText(initialTimeInMilli[2], bonusTimeInMilli[2])
                        customBtn3.setOnClickListener{onButtonClicked(initialTimeInMilli[2], bonusTimeInMilli[2])}
                    }
                }
            }
        })
    }

    private fun getFormattedText(initialTime: Long, bonusTime: Long): CharSequence? {
        if (bonusTime != 0L) {
            return "${initialTime/60000} | ${bonusTime/1000}"
        }

        return "${initialTime/60000} min"
    }

    private fun onButtonClicked(initialTime: Long, bonusTime: Long) {
        val action = DashboardFragmentDirections.actionDashboardFragmentToClockFragment(initialTime, bonusTime)
        findNavController().navigate(action)
    }


}