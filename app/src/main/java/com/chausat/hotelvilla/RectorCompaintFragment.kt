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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RectorCompaintFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rector_compaint, container, false)

        val arrayListRectorComplaintHistory = ArrayList<RectorComplaintHistory>()
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.custom_loading)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("${MainClass().getURL()}rectorComplaintHistory.php")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                activity!!.runOnUiThread {
                    val data = response.body?.string() as String
                    Log.d("Data", data)
                    val JsonObject = JSONObject(data)
                    val success = JsonObject.getInt("success")
                    if (success == 1) {
                       val jsonDataArray = JsonObject.getJSONArray("complaintHistory")
                        for(i in 0 until jsonDataArray.length()){
                            arrayListRectorComplaintHistory.add(
                                    RectorComplaintHistory(
                                            jsonDataArray.getJSONObject(i).getString("c_id").toInt(),jsonDataArray.getJSONObject(i).getString("stud_id").toInt(),
                                            jsonDataArray.getJSONObject(i).getString("stud_name"),jsonDataArray.getJSONObject(i).getString("hostel_no").toInt(),
                                            jsonDataArray.getJSONObject(i).getString("room_no").toInt(),jsonDataArray.getJSONObject(i).getString("subject"),
                                            jsonDataArray.getJSONObject(i).getString("decsp"),jsonDataArray.getJSONObject(i).getString("isSeen").toInt()
                                    )
                            )
                        }
                        val recyclerViewRectorComplaint = view.findViewById<RecyclerView>(R.id.recyclerViewRectorComplaint)
                        recyclerViewRectorComplaint.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                        recyclerViewRectorComplaint.adapter = RecyclerViewRectorCompalintHistory(arrayListRectorComplaintHistory, activity!!)
                        dialog.dismiss()
                    }
                }
            }
        })
        return view
    }
}