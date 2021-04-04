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

class FacultyDashboardActivity : AppCompatActivity() {

    lateinit var textViewFacultyName : AppCompatTextView
    lateinit var textViewFacultyID : AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_dashboard)

        textViewFacultyName = findViewById(R.id.textViewFacultyName)
        textViewFacultyID = findViewById(R.id.textViewFacultyID)

        loadData()

        findViewById<CardView>(R.id.cardViewStudentPassManagement).setOnClickListener {
            startActivity(Intent(this, FacultyPassActivity::class.java))
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
                    val dialog = Dialog(this@FacultyDashboardActivity)
                    dialog.setContentView(R.layout.custom_loading)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show()
                    val data = response.body?.string() as String
                    Log.d("Data", data)
                    val JsonObject = JSONObject(data)
                    val success = JsonObject.getInt("success")
                    if (success == 1) {
                        textViewFacultyName.text = JsonObject.getString("name")
                        textViewFacultyID.text = JsonObject.getString("id")
                    }
                    dialog.dismiss()
                }
            }
        })
    }

}