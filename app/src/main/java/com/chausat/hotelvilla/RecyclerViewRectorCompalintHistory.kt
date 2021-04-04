package com.chausat.hotelvilla

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RecyclerViewRectorCompalintHistory(var arrayListRectorComplaintHistory: ArrayList<RectorComplaintHistory>, var activity: FragmentActivity)
    : RecyclerView.Adapter<RecyclerViewRectorCompalintHistory.ViewHolderRectorComplaintHistory>(){

        lateinit var view : Context

        inner class ViewHolderRectorComplaintHistory(view : View) : RecyclerView.ViewHolder(view){
            val textViewStudentName = view.findViewById<TextView>(R.id.textViewStudentName)
            val textViewStudentHostelNumber = view.findViewById<TextView>(R.id.textViewStudentHostelNumber)
            val textViewStudentRoomNumber = view.findViewById<TextView>(R.id.textViewStudentRoomNumber)
            val textViewStudentSubject = view.findViewById<TextView>(R.id.textViewStudentSubject)
            val textViewStudentExplain = view.findViewById<TextView>(R.id.textViewStudentExplain)
            val buttonMarkAsSeen = view.findViewById<AppCompatButton>(R.id.buttonMarkAsSeen)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRectorComplaintHistory {
        view = parent.context
        return ViewHolderRectorComplaintHistory(
                LayoutInflater.from(parent.context).inflate(R.layout.rector_complain_history_recyclerview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderRectorComplaintHistory, position: Int) {
        holder.textViewStudentExplain.movementMethod = ScrollingMovementMethod()

        if(arrayListRectorComplaintHistory[position].isSeen == 0){
            holder.buttonMarkAsSeen.visibility = View.VISIBLE
        }else{
            holder.buttonMarkAsSeen.visibility = View.GONE
        }

        holder.textViewStudentName.text = arrayListRectorComplaintHistory[position].studentName
        holder.textViewStudentHostelNumber.text = arrayListRectorComplaintHistory[position].studentHostel.toString()
        holder.textViewStudentRoomNumber.text = arrayListRectorComplaintHistory[position].studentRoomNo.toString()
        holder.textViewStudentSubject.text = arrayListRectorComplaintHistory[position].subject
        holder.textViewStudentExplain.text = arrayListRectorComplaintHistory[position].description

        holder.buttonMarkAsSeen.setOnClickListener {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url("${MainClass().getURL()}rectorComplaintSeen.php?student_id=${arrayListRectorComplaintHistory[position].studentId}&c_id=${arrayListRectorComplaintHistory[position].Id}")
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    activity.runOnUiThread {
                        val dialog = Dialog(activity)
                        dialog.setContentView(R.layout.custom_loading)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.show()
                        val data = response.body?.string() as String
                        Log.d("Data", data)
                        val JsonObject = JSONObject(data)
                        val success = JsonObject.getInt("success")
                        if (success == 1) {
                            holder.buttonMarkAsSeen.visibility = View.GONE
                        }
                        dialog.dismiss()
                    }
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return arrayListRectorComplaintHistory.size
    }

}