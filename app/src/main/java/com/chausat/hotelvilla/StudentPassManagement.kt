package com.chausat.hotelvilla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class StudentPassManagement : AppCompatActivity() {
    lateinit var viewPagerPassData : ViewPager
    lateinit var tabLayoutPassMenu : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_pass_management)

        viewPagerPassData = findViewById(R.id.viewPagerPassData)
        tabLayoutPassMenu = findViewById(R.id.tabLayoutPassMenu)

        val fragmentManger = StudentPassManagementAdapter(supportFragmentManager)
        viewPagerPassData.adapter = fragmentManger

        tabLayoutPassMenu.setupWithViewPager(viewPagerPassData)
    }
}