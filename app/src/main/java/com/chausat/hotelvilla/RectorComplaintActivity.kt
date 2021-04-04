package com.chausat.hotelvilla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class RectorComplaintActivity : AppCompatActivity() {

    lateinit var viewPagerRectorNotification : ViewPager
    lateinit var tabLayoutRectorNotification : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rector_complaint)

        viewPagerRectorNotification = findViewById(R.id.viewPagerRectorNotification)
        tabLayoutRectorNotification = findViewById(R.id.tabLayoutRectorNotification)

        val fragmentManger = RectorComplaintAdapter(supportFragmentManager)
        viewPagerRectorNotification.adapter = fragmentManger

        tabLayoutRectorNotification.setupWithViewPager(viewPagerRectorNotification)

    }
}