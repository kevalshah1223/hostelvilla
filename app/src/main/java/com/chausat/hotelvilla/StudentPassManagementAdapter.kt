package com.chausat.hotelvilla

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class StudentPassManagementAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                StudentTakePassFragment()
            }
            1->{
                StudentPassHistoryFragment()
            }
            else -> {
                StudentTakePassFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0->{
                "Take Pass"
            }
            1->{
                "History"
            }

            else ->{
                "Take Pass"
            }
        }
    }
}