package com.chausat.hotelvilla

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    companion object {
        const val LOGIN = "LOGIN"
        const val USER_ROLE = "USER_ROLE"
        const val USER_ID = "USER_ID"
        const val USER_PASS = "USER_PASS"
        const val USER_UNIQUE_ID = "USER_UNIQUE_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextUserName = findViewById<AppCompatEditText>(R.id.editTextUserName)
        val editTextUserPassword = findViewById<AppCompatEditText>(R.id.editTextUserPassword)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val pref = getSharedPreferences(LOGIN, MODE_PRIVATE)
            val edit = pref.edit()
            if (editTextUserName.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Enter Username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (editTextUserPassword.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (MainClass().isConnected(applicationContext) == true) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${MainClass().getURL()}login.php?username=${editTextUserName.text.toString()}&password=${editTextUserPassword.text.toString()}")
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        runOnUiThread {
                            val dialog = Dialog(this@LoginActivity)
                            dialog.setContentView(R.layout.custom_loading)
                            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialog.show()
                            val data = response.body?.string() as String
                            Log.d("Data", data)
                            val JsonObject = JSONObject(data)
                            val success = JsonObject.getInt("success")
                            if (success == 1) {
                                val role = JsonObject.getString("role")
                                val id = JsonObject.getString("id")
                                edit.putString(USER_ROLE, role)
                                edit.putString(USER_UNIQUE_ID, id)
                                edit.putString(USER_ID, editTextUserName.text.toString())
                                edit.putString(USER_PASS, editTextUserPassword.text.toString())
                                when (role) {
                                    "f" -> {
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                FacultyDashboardActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                    "r" -> {
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                RectorDashboardActivity::class.java
                                            )
                                        )
                                        finish()
                                    }

                                    "a" -> {
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                AdminDashboardActivity::class.java
                                            )
                                        )
                                        finish()
                                    }

                                    else -> {
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                StudentDashboardActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                }
                                edit.apply()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Invalid Username/Password, Try Again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            dialog.dismiss()
                        }
                    }
                })
            } else {
                Toast.makeText(
                    applicationContext,
                    "No Internet Connection, Try Again",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}