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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentPassHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class StudentPassHistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerViewPassHistory : RecyclerView
    lateinit var textViewNoPassApplied : AppCompatTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_pass_history, container, false)
        textViewNoPassApplied = view.findViewById(R.id.textViewNoPassApplied)
        recyclerViewPassHistory = view.findViewById(R.id.recyclerViewPassHistory)

        loadData()

        return view
    }

    private fun loadData() {
        val arrayListStudentPassHistory = ArrayList<StudentPassHistory>()
        val pref = activity!!.getSharedPreferences(LoginActivity.LOGIN, AppCompatActivity.MODE_PRIVATE)
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("${MainClass().getURL()}studentPassHistory.php?studId=${pref.getString(LoginActivity.USER_UNIQUE_ID,null)}")
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
                        val studentPassHistoryData = JsonObject.getJSONArray("passHistory")

                        for(i in 0 until studentPassHistoryData.length()){
                            arrayListStudentPassHistory.add(
                                    StudentPassHistory(studentPassHistoryData.getJSONObject(i).getString("p_id"), studentPassHistoryData.getJSONObject(i).getString("s_id").toInt(),
                                            studentPassHistoryData.getJSONObject(i).getString("leave_type").toInt(),studentPassHistoryData.getJSONObject(i).getString("data_1"),
                                            studentPassHistoryData.getJSONObject(i).getString("data_2"),studentPassHistoryData.getJSONObject(i).getString("isRectorApproved").toInt(),studentPassHistoryData.getJSONObject(i).getString("isFacultyApproved").toInt(),
                                            studentPassHistoryData.getJSONObject(i).getString("isCanceled").toInt(),studentPassHistoryData.getJSONObject(i).getString("destination"),studentPassHistoryData.getJSONObject(i).getString("purpose")
                                    )
                            )
                        }
                        recyclerViewPassHistory.layoutManager = LinearLayoutManager(view!!.context, RecyclerView.VERTICAL, false)
                        recyclerViewPassHistory.adapter = RecyclerViewStudentPassHistory(arrayListStudentPassHistory, activity!!)
                        textViewNoPassApplied.visibility = View.GONE
                        recyclerViewPassHistory.visibility = View.VISIBLE
                    }else{
                        recyclerViewPassHistory.visibility = View.GONE
                        textViewNoPassApplied.visibility = View.VISIBLE
                    }
                    dialog.dismiss()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentPassHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentPassHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}