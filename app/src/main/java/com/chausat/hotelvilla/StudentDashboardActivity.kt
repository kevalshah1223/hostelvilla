package com.chausat.hotelvilla

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class StudentDashboardActivity : AppCompatActivity() {

    lateinit var textViewStudentName : AppCompatTextView
    lateinit var textViewStudentID : AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        textViewStudentName = findViewById(R.id.textViewStudentName)
        textViewStudentID = findViewById(R.id.textViewStudentID)

        val cardViewPassManagement = findViewById<CardView>(R.id.cardViewNotification)
        val cardViewComplainManagement = findViewById<CardView>(R.id.cardViewComplainManagement)
        loadData()
        cardViewPassManagement.setOnClickListener {
            startActivity(Intent(this, StudentPassManagement::class.java))
        }

        cardViewComplainManagement.setOnClickListener {
            startActivity(Intent(this, StudentComplaintManagementActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.buttonLogout).setOnClickListener {
            val pref = getSharedPreferences(LoginActivity.LOGIN, MODE_PRIVATE)
            val edit = pref.edit()
            edit.clear()
            edit.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    private fun loadData() {
        val pref = getSharedPreferences(LoginActivity.LOGIN, MODE_PRIVATE)
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("${MainClass().getURL()}loaddata.php?username=${pref.getString(LoginActivity.USER_ID,null)}&password=${pref.getString(LoginActivity.USER_PASS,null)}&role=${pref.getString(LoginActivity.USER_ROLE,null)}")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    val dialog = Dialog(this@StudentDashboardActivity)
                    dialog.setContentView(R.layout.custom_loading)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show()
                    val data = response.body?.string() as String
                    Log.d("Data", data)
                    val JsonObject = JSONObject(data)
                    val success = JsonObject.getInt("success")
                    if (success == 1) {
                        textViewStudentName.text = JsonObject.getString("name")
                        textViewStudentID.text = JsonObject.getString("id")
                    }
                    dialog.dismiss()
                }
            }
        })
    }
}