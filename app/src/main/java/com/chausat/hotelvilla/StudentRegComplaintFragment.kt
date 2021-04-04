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
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentRegComplaintFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentRegComplaintFragment : Fragment() {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_reg_complaint, container, false)

        val arrayListComplaint = ArrayList<ComplaintInformation>()

        val pref = activity!!.getSharedPreferences(LoginActivity.LOGIN, AppCompatActivity.MODE_PRIVATE)
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("${MainClass().getURL()}studentComplaintHistory.php?studId=${pref.getString(LoginActivity.USER_UNIQUE_ID,null)}")
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
                        val complaintHis = JsonObject.getJSONArray("complaintHistory")
                        val textViewNoData = view.findViewById<AppCompatTextView>(R.id.textViewNoData)
                        val recyclerViewComplaint = view.findViewById<RecyclerView>(R.id.recyclerViewComplaint)
                        if(complaintHis.length() == 0){
                            textViewNoData.visibility = View.VISIBLE
                            recyclerViewComplaint.visibility = View.GONE
                        }else {

                            for (i in 0 until complaintHis.length()) {
                                arrayListComplaint.add(
                                        ComplaintInformation(
                                                complaintHis.getJSONObject(i).getString("c_id").toInt(), complaintHis.getJSONObject(i).getString("s_id").toInt(),
                                                complaintHis.getJSONObject(i).getString("hostel_no").toInt(), complaintHis.getJSONObject(i).getString("room_no").toInt(),
                                                complaintHis.getJSONObject(i).getString("subject"), complaintHis.getJSONObject(i).getString("complaint"), complaintHis.getJSONObject(i).getString("isSeen").toInt()
                                        )
                                )
                            }
                            recyclerViewComplaint.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                            recyclerViewComplaint.adapter = RecyclerViewStudentComplaintHistory(arrayListComplaint, activity!!)

                            textViewNoData.visibility = View.GONE
                            recyclerViewComplaint.visibility = View.VISIBLE
                        }
                    }
                    dialog.dismiss()
                }
            }
        })
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentRegComplaintFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                StudentRegComplaintFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}