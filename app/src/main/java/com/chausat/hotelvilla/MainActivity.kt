package com.chausat.hotelvilla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val thread = Thread {
            try {
                Thread.sleep(2000)

                val pref = getSharedPreferences("LOGIN", MODE_PRIVATE)
                when {
                    pref.getString("USER_ROLE",null) == null -> {
                        startActivity(
                            Intent(this, LoginActivity::class.java)
                        )
                    }
                    pref.getString("USER_ROLE",null) == "f" -> {
                        startActivity(
                            Intent(this, FacultyDashboardActivity::class.java)
                        )
                    }
                    pref.getString("USER_ROLE",null) == "r" -> {
                        startActivity(
                            Intent(this, RectorDashboardActivity::class.java)
                        )
                    }
                    pref.getString("USER_ROLE",null) == "a" -> {
                        startActivity(
                            Intent(this, AdminDashboardActivity::class.java)
                        )
                    }
                    else -> {
                        startActivity(
                            Intent(this, StudentDashboardActivity::class.java)
                        )
                    }
                }
                finish()
            }catch (ep : Exception){
                ep.printStackTrace()
            }
        }
        thread.start()
    }
}