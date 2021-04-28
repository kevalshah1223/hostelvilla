package com.chausat.hotelvilla

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AddFacultyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_faculty)

        val textInputLayoutFacultyName =
            findViewById<TextInputLayout>(R.id.textInputLayoutFacultyName)
        val textInputLayoutFacultyContactNumber =
            findViewById<TextInputLayout>(R.id.textInputLayoutFacultyContactNumber)

        findViewById<AppCompatButton>(R.id.buttonRegisterFaculty).setOnClickListener {
            if (textInputLayoutFacultyName.editText!!.text.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Faculty Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (textInputLayoutFacultyContactNumber.editText!!.text.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Faculty Number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (MainClass().isConnected(this) == true) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${MainClass().getURL()}insertFaculty.php?facultyName=${textInputLayoutFacultyName.editText!!.text.toString()}&facultyNumber=${textInputLayoutFacultyContactNumber.editText!!.text.toString()}")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        runOnUiThread {
                            val data = response.body?.string() as String
                            val JsonObject = JSONObject(data)
                            val success = JsonObject.getInt("success")
                            if (success == 1) {
                                val uniqueId = JsonObject.getString("username")
                                val alert = AlertDialog.Builder(it.context)
                                val layoutInflater = LayoutInflater.from(it.context)
                                val view = layoutInflater.inflate(
                                    R.layout.custom_view_student_info_design,
                                    null
                                )
                                Snackbar.make(it, "Username: $uniqueId \n Password: ${textInputLayoutFacultyContactNumber.editText!!.text.toString()}", Snackbar.LENGTH_SHORT).show()
                                val textViewUsername = view.findViewById<AppCompatTextView>(R.id.textViewUsername)
                                val textHostelNo = view.findViewById<AppCompatTextView>(R.id.textHostelNo)
                                val textViewPassword = view.findViewById<AppCompatTextView>(R.id.textViewPassword)
                                textHostelNo.text = "New Faculty Details"
                                textViewUsername.text = uniqueId
                                textViewPassword.text = textInputLayoutFacultyContactNumber.editText!!.text.toString()
                                val buttonDone = view.findViewById<Button>(R.id.buttonDone)
                                alert.setView(view)
                                val alertDialog = alert.create()
                                alertDialog.setCanceledOnTouchOutside(false)
                                alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                                alertDialog.show()
                                textInputLayoutFacultyContactNumber.editText!!.text.clear()
                                textInputLayoutFacultyName.editText!!.text.clear()
                                buttonDone.setOnClickListener {
                                    alertDialog.dismiss()
                                }
                            }
                        }
                    }

                })

            } else {
                Toast.makeText(this, "No Internet Connection, Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}