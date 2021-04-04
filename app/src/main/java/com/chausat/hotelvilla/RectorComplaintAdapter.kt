package com.chausat.hotelvilla

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class RectorComplaintAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 1
    }

    override fun getItem(position: Int): Fragment {
        return RectorCompaintFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Complaint"
    }
}