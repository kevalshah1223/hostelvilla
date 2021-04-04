package com.chausat.hotelvilla

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FacultyPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_pass)

        val recyclerViewFacultyComplaint = findViewById<RecyclerView>(R.id.recyclerViewFacultyComplaint)

        val arrayListRectorPassHistory = ArrayList<RectorPassHistory>()

        val client = OkHttpClient()
        val request = Request.Builder()
                .url("${MainClass().getURL()}facultyPassHistory.php")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                this@FacultyPassActivity.runOnUiThread {
                    val dialog = Dialog(this@FacultyPassActivity)
                    dialog.setContentView(R.layout.custom_loading)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show()
                    val data = response.body?.string() as String
                    Log.d("Data", data)
                    val JsonObject = JSONObject(data)
                    val success = JsonObject.getInt("success")
                    if (success == 1) {
                        val jsonData = JsonObject.getJSONArray("passHistory")
                        for(i in 0 until jsonData.length()){
                            arrayListRectorPassHistory.add(
                                    RectorPassHistory(
                                            jsonData.getJSONObject(i).getString("p_id").toInt(),jsonData.getJSONObject(i).getString("stus_id").toInt(),
                                            jsonData.getJSONObject(i).getString("stus_name"),jsonData.getJSONObject(i).getString("hostel_no").toInt(),
                                            jsonData.getJSONObject(i).getString("room_no").toInt(),jsonData.getJSONObject(i).getString("leave_type").toInt(),
                                            jsonData.getJSONObject(i).getString("data_1"), jsonData.getJSONObject(i).getString("data_2"),
                                            jsonData.getJSONObject(i).getString("destination"), jsonData.getJSONObject(i).getString("purpose"),
                                            jsonData.getJSONObject(i).getString("isRectorApproved").toInt(), jsonData.getJSONObject(i).getString("isFacultyApproved").toInt(),
                                            jsonData.getJSONObject(i).getString("isCanceled").toInt()
                                    )
                            )
                        }
                        recyclerViewFacultyComplaint.layoutManager = LinearLayoutManager(this@FacultyPassActivity, RecyclerView.VERTICAL, false)
                        recyclerViewFacultyComplaint.adapter = RecyclerViewFacultyPassHistory(arrayListRectorPassHistory, this@FacultyPassActivity)
                    }
                    dialog.dismiss()
                }
            }
        })
    }
}