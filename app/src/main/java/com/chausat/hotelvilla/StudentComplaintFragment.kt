package com.chausat.hotelvilla

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputLayout
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentComplaintFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentComplaintFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: 1")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_complaint, container, false)
        val textInputLayoutHostelNo = view.findViewById<TextInputLayout>(R.id.textInputLayoutHostelNo)
        val textInputLayoutRoomNo = view.findViewById<TextInputLayout>(R.id.textInputLayoutRoomNo)
        val textInputLayoutSubject = view.findViewById<TextInputLayout>(R.id.textInputLayoutSubject)
        val textInputLayoutExplanation = view.findViewById<TextInputLayout>(R.id.textInputLayoutExplanation)
        val buttonRegisterStudent = view.findViewById<AppCompatButton>(R.id.buttonRegisterStudent)

        buttonRegisterStudent.setOnClickListener {
            val pref = activity!!.getSharedPreferences(LoginActivity.LOGIN, AppCompatActivity.MODE_PRIVATE)

            if(textInputLayoutHostelNo.editText!!.text.isEmpty()){
                Toast.makeText(activity!!, "Enter Hostel NO.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(textInputLayoutRoomNo.editText!!.text.isEmpty()){
                Toast.makeText(activity!!, "Enter Room NO.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(textInputLayoutSubject.editText!!.text.isEmpty()){
                Toast.makeText(activity!!, "Enter Complaint Subject.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(textInputLayoutExplanation.editText!!.text.isEmpty()){
                Toast.makeText(activity!!, "Enter Complaint Content.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val client = OkHttpClient()
            val request = Request.Builder()
                    .url("${MainClass().getURL()}insertComplaint.php?studID=${pref.getString(LoginActivity.USER_ID, null)}&studPass=${pref.getString(LoginActivity.USER_PASS, null)}&Hostel_no=${textInputLayoutHostelNo.editText!!.text.toString()}&room_no=${textInputLayoutRoomNo.editText!!.text.toString()}&subject=${textInputLayoutSubject.editText!!.text.toString()}&complaint=${textInputLayoutExplanation.editText!!.text.toString()}")
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    activity!!.runOnUiThread {
                        val dialog = Dialog(activity!!)
                        dialog.setContentView(R.layout.custom_loading)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.show()
                        val data = response.body?.string() as String
                        Log.d("Data", data)
                        val JsonObject = JSONObject(data)
                        val success = JsonObject.getInt("success")
                        if (success == 1) {
                            Log.d("TAG", "onResponse: Applied")
                            Toast.makeText(activity!!, "Your complaint is Registered", Toast.LENGTH_SHORT).show()
                            textInputLayoutHostelNo.editText!!.text.clear()
                            textInputLayoutRoomNo.editText!!.text.clear()
                            textInputLayoutSubject.editText!!.text.clear()
                            textInputLayoutExplanation.editText!!.text.clear()
                        } else {
                            Log.d("TAG", "onResponse: Failed")
                            Toast.makeText(activity!!, "Error in Registering Complaint", Toast.LENGTH_SHORT).show()
                        }

                        dialog.dismiss()
                    }
                }
            })
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentComplaintFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentComplaintFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}