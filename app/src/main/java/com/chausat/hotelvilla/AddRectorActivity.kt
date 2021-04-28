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

class AddRectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rector)

        val textInputLayoutRectorName = findViewById<TextInputLayout>(R.id.textInputLayoutRectorName)
        val textInputLayoutRectorNumber = findViewById<TextInputLayout>(R.id.textInputLayoutRectorNumber)
        val textInputLayoutRectorHostelNo = findViewById<TextInputLayout>(R.id.textInputLayoutRectorHostelNo)

        findViewById<AppCompatButton>(R.id.buttonRegisterRector).setOnClickListener {
            if (MainClass().isConnected(this) == true) {
                if (textInputLayoutRectorName.editText!!.text.isNullOrEmpty()) {
                    Toast.makeText(this, "Enter Rector Name", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (textInputLayoutRectorNumber.editText!!.text.isNullOrEmpty()) {
                    Toast.makeText(this, "Enter Rector Number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (textInputLayoutRectorHostelNo.editText!!.text.isNullOrEmpty()) {
                    Toast.makeText(this, "Enter Rector Hostel No", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${MainClass().getURL()}insertRector.php?Name=${textInputLayoutRectorName.editText!!.text.toString()}&Number=${textInputLayoutRectorNumber.editText!!.text.toString()}&HostelNo=${textInputLayoutRectorHostelNo.editText!!.text.toString()}")
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
                                val alert = AlertDialog.Builder(it.context)
                                val layoutInflater = LayoutInflater.from(it.context)
                                val view = layoutInflater.inflate(
                                    R.layout.custom_view_student_info_design,
                                    null
                                )
                                Snackbar.make(it, "Username: ${textInputLayoutRectorHostelNo.editText!!.text.toString()} \n Password: ${textInputLayoutRectorNumber.editText!!.text.toString()}", Snackbar.LENGTH_SHORT).show()
                                val textViewUsername = view.findViewById<AppCompatTextView>(R.id.textViewUsername)
                                val textHostelNo = view.findViewById<AppCompatTextView>(R.id.textHostelNo)
                                val textViewPassword = view.findViewById<AppCompatTextView>(R.id.textViewPassword)
                                textHostelNo.text = "New Rector Details"
                                textViewUsername.text = textInputLayoutRectorHostelNo.editText!!.text.toString()
                                textViewPassword.text = textInputLayoutRectorNumber.editText!!.text.toString()
                                val buttonDone = view.findViewById<Button>(R.id.buttonDone)
                                alert.setView(view)
                                val alertDialog = alert.create()
                                alertDialog.setCanceledOnTouchOutside(false)
                                alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                                alertDialog.show()
                                textInputLayoutRectorName.editText!!.text.clear()
                                textInputLayoutRectorNumber.editText!!.text.clear()
                                textInputLayoutRectorHostelNo.editText!!.text.clear()
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