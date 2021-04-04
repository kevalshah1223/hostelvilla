package com.chausat.hotelvilla

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RectorNotificationActivity : AppCompatActivity() {

    lateinit var viewPagerRectorNotification : ViewPager
    lateinit var tabLayoutRectorNotification : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rector_notification)
/*
        val recyclerViewRectorPass = findViewById<RecyclerView>(R.id.recyclerViewRectorPass)
        val arrayListRectorPassHistory = ArrayList<RectorPassHistory>()
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("${MainClass().getURL()}rectorPassHistory.php")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
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
                        recyclerViewRectorPass.layoutManager = LinearLayoutManager(this@RectorNotificationActivity, RecyclerView.VERTICAL, false)
                        recyclerViewRectorPass.adapter = RecyclerViewRectorPassHistory(arrayListRectorPassHistory, this@RectorNotificationActivity)
                    }
                }
            }
        })*/

        viewPagerRectorNotification = findViewById(R.id.viewPagerRectorNotification)
        tabLayoutRectorNotification = findViewById(R.id.tabLayoutRectorNotification)

        val fragmentManger = RectorNotificationAdapter(supportFragmentManager)
        viewPagerRectorNotification.adapter = fragmentManger

        tabLayoutRectorNotification.setupWithViewPager(viewPagerRectorNotification)

    }
}