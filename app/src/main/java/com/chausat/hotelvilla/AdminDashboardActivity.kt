package com.chausat.hotelvilla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        findViewById<AppCompatButton>(R.id.buttonLogout).setOnClickListener {
            val pref = getSharedPreferences(LoginActivity.LOGIN, MODE_PRIVATE)
            val edit = pref.edit()
            edit.clear()
            edit.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        findViewById<CardView>(R.id.cardViewAddFaculty).setOnClickListener {
            startActivity(Intent(this, AddFacultyActivity::class.java))
        }

        findViewById<CardView>(R.id.cardViewAddRector).setOnClickListener {
            startActivity(Intent(this, AddRectorActivity::class.java))
        }
    }
}