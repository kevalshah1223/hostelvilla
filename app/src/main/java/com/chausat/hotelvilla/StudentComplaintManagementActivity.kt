package com.chausat.hotelvilla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class StudentComplaintManagementActivity : AppCompatActivity() {

    lateinit var viewPagerComplainData : ViewPager
    lateinit var tabLayoutComplainMenu : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_complaint_management)

        viewPagerComplainData = findViewById(R.id.viewPagerComplainData)
        tabLayoutComplainMenu = findViewById(R.id.tabLayoutComplainMenu)

        val fragmentManger = StudentComplaintManagementAdapter(supportFragmentManager)
        viewPagerComplainData.adapter = fragmentManger
        viewPagerComplainData.offscreenPageLimit = 0
        tabLayoutComplainMenu.setupWithViewPager(viewPagerComplainData)

    }
}