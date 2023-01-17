package com.example.safeapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.safeapp.CallesFragment
import com.example.safeapp.ChackInFragment
import com.example.safeapp.ResidFragment
import com.example.safeapp.fraccFragment

class AdapterCheckIn(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            ChackInFragment()
        }else{
            if (position == 1){
              ResidFragment()
            }else{
                if (position == 2){
                    CallesFragment()
                }else{
                   fraccFragment()
                }
            }
        }
    }
}