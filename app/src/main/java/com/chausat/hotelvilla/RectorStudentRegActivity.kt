package com.chausat.hotelvilla

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.text.SpannableString
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RectorStudentRegActivity : AppCompatActivity() {

    companion object {
        var permisson_flag = false
    }

    lateinit var textViewRegisterStudent: TextView
    lateinit var textInputLayoutStudentName: TextInputLayout
    lateinit var textInputLayoutStudentContactNumber: TextInputLayout
    lateinit var textInputLayoutStudentParentName: TextInputLayout
    lateinit var textInputLayoutStudentParentPhoneNumber: TextInputLayout
    lateinit var textInputLayoutStudentHostelNumber: TextInputLayout
    lateinit var textInputLayoutStudentRoomNumber: TextInputLayout
    lateinit var textInputLayoutStudentAddress: TextInputLayout
    lateinit var buttonRegisterStudent: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rector_student_reg)

        textViewRegisterStudent = findViewById(R.id.textViewRegisterStudent)
        textInputLayoutStudentName = findViewById(R.id.textInputLayoutStudentName)
        textInputLayoutStudentContactNumber = findViewById(R.id.textInputLayoutStudentContactNumber)
        textInputLayoutStudentParentName = findViewById(R.id.textInputLayoutStudentParentName)
        textInputLayoutStudentParentPhoneNumber =
            findViewById(R.id.textInputLayoutStudentParentPhoneNumber)
        textInputLayoutStudentHostelNumber = findViewById(R.id.textInputLayoutStudentHostelNumber)
        textInputLayoutStudentRoomNumber = findViewById(R.id.textInputLayoutStudentRoomNumber)
        textInputLayoutStudentAddress = findViewById(R.id.textInputLayoutStudentAddress)
        buttonRegisterStudent = findViewById(R.id.buttonRegisterStudent)

        val spannableTextViewRegisterStudent =
            SpannableString(textViewRegisterStudent.text.toString())
        spannableTextViewRegisterStudent.setSpan(
            UnderlineSpan(),
            0,
            textViewRegisterStudent.text.length,
            0
        )
        textViewRegisterStudent.text = spannableTextViewRegisterStudent


        buttonRegisterStudent.setOnClickListener {
            if (textInputLayoutStudentName.editText!!.text.isEmpty()) {
                Toast.makeText(this, "Enter Student Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (textInputLayoutStudentContactNumber.editText!!.text.isEmpty()) {
                Toast.makeText(this, "Enter Student Contact Number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (textInputLayoutStudentParentName.editText!!.text.isEmpty()) {
                Toast.makeText(this, "Enter Parent Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (textInputLayoutStudentParentPhoneNumber.editText!!.text.isEmpty()) {
                Toast.makeText(this, "Enter Parent Contact Number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (textInputLayoutStudentHostelNumber.editText!!.text.isEmpty()) {
                Toast.makeText(this, "Enter Hostel No.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (textInputLayoutStudentRoomNumber.editText!!.text.isEmpty()) {
                Toast.makeText(this, "Enter Room No.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (textInputLayoutStudentAddress.editText!!.text.isEmpty()) {
                Toast.makeText(this, "Enter Student Address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (MainClass().isConnected(applicationContext) == true) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${MainClass().getURL()}insertnewstudent.php?studName=${textInputLayoutStudentName.editText!!.text.toString()}&studNumber=${textInputLayoutStudentContactNumber.editText!!.text.toString()}&parentName=${textInputLayoutStudentParentName.editText!!.text.toString()}&parentNumber=${textInputLayoutStudentParentPhoneNumber.editText!!.text.toString()}&hostelNo=${textInputLayoutStudentHostelNumber.editText!!.text.toString()}&roomNo=${textInputLayoutStudentRoomNumber.editText!!.text.toString()}&address=${textInputLayoutStudentAddress.editText!!.text.toString()}")
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        runOnUiThread {
                            val dialog = Dialog(this@RectorStudentRegActivity)
                            dialog.setContentView(R.layout.custom_loading)
                            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialog.show()
                            val data = response.body?.string() as String
                            Log.d("Data", data)
                            val JsonObject = JSONObject(data)
                            val success = JsonObject.getInt("success")
                            if (success == 1) {
                                Toast.makeText(
                                    applicationContext,
                                    "Student Register",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val alert = AlertDialog.Builder(it.context)
                                val layoutInflater = LayoutInflater.from(it.context)
                                val view = layoutInflater.inflate(
                                    R.layout.custom_view_student_info_design,
                                    null
                                )

                                 val textViewUsername = view.findViewById<AppCompatTextView>(R.id.textViewUsername)
                                 val textViewPassword = view.findViewById<AppCompatTextView>(R.id.textViewPassword)

                                 textViewUsername.text = textInputLayoutStudentRoomNumber.editText!!.text.toString()
                                 textViewPassword.text = textInputLayoutStudentContactNumber.editText!!.text.toString()
                                val buttonDone = view.findViewById<Button>(R.id.buttonDone)
                                alert.setView(view)
                                val alertDialog = alert.create()
                                alertDialog.setCanceledOnTouchOutside(false)
                                alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                                alertDialog.show()
                                buttonDone.setOnClickListener {
                                    alertDialog.dismiss()
                                }
                                clearAllData()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Error in registering data",
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

    fun clearAllData() {
        textInputLayoutStudentName.editText!!.text.clear()
        textInputLayoutStudentContactNumber.editText!!.text.clear()
        textInputLayoutStudentParentName.editText!!.text.clear()
        textInputLayoutStudentParentPhoneNumber.editText!!.text.clear()
        textInputLayoutStudentHostelNumber.editText!!.text.clear()
        textInputLayoutStudentRoomNumber.editText!!.text.clear()
        textInputLayoutStudentAddress.editText!!.text.clear()
    }
}