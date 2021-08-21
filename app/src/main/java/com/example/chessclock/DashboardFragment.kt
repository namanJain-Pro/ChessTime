package com.example.chessclock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.chessclock.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDashboardBinding.bind(view)

        binding.apply {

            //Bullet
            bulletBtn1.setOnClickListener{_ ->  {}}

        }
    }
}