package com.chausat.hotelvilla

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class StudentComplaintManagementAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                StudentComplaintFragment()
            }
            1->{
                StudentRegComplaintFragment()
            }
            else -> {
                StudentComplaintFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0->{
                "Complaint"
            }
            1->{
                "Notification"
            }
            else ->{
                "Complaint"
            }
        }
    }
}