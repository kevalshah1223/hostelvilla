package com.chausat.hotelvilla

import android.app.AlertDialog
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*


class RecyclerViewStudentPassHistory(private val arrayListStudentPassHistory: ArrayList<StudentPassHistory>, val activity: FragmentActivity) : RecyclerView.Adapter<RecyclerViewStudentPassHistory.ViewHolderStudentPassHistory>() {

    lateinit var view : Context

    inner class ViewHolderStudentPassHistory(view: View) : RecyclerView.ViewHolder(view){
        val textViewLeaveType = view.findViewById<TextView>(R.id.textViewLeaveType)
        val textViewStartDateText = view.findViewById<TextView>(R.id.textViewData1Text)
        val textViewStartDate = view.findViewById<TextView>(R.id.textViewData1)
        val textViewEndDateText = view.findViewById<TextView>(R.id.textViewDataText2)
        val textViewEndDate = view.findViewById<TextView>(R.id.textViewData2)
        val textViewFacultyText = view.findViewById<TextView>(R.id.textViewFaculty)
        val textViewFacultyIsAccepted = view.findViewById<TextView>(R.id.textViewFacultyIsApproved)
        val textViewRectorText = view.findViewById<TextView>(R.id.textViewRector)
        val textViewRectorIsAccepted = view.findViewById<TextView>(R.id.textViewRectorIsApproved)
        val buttonCancelPass = view.findViewById<Button>(R.id.buttonCancelPass)
        val buttonViewPass = view.findViewById<Button>(R.id.buttonViewPass)
        val groupButtonActions = view.findViewById<Group>(R.id.groupButtonActions)
        val textViewYourPassIsCanceled = view.findViewById<TextView>(R.id.textViewYourPassIsCanceled)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStudentPassHistory {
        view = parent.context
        return ViewHolderStudentPassHistory(LayoutInflater.from(parent.context).inflate(R.layout.student_pass_history_recyclerview, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderStudentPassHistory, position: Int) {

        if(arrayListStudentPassHistory[position].isCanceled == 1){
            holder.groupButtonActions.visibility = View.GONE
            holder.textViewYourPassIsCanceled.visibility = View.VISIBLE
        }
        if(arrayListStudentPassHistory[position].leaveType == 0){
            holder.textViewLeaveType.text = view.getString(R.string.home_pass)
            holder.textViewStartDateText.text = view.getString(R.string.start_date)
            holder.textViewEndDateText.text = view.getString(R.string.end_date)
            holder.textViewRectorText.text = view.getString(R.string.rector)
            holder.textViewFacultyText.text = view.getString(R.string.faculty)
            /////////////////////////
            holder.textViewStartDate.text = arrayListStudentPassHistory[position].passData1
            holder.textViewEndDate.text = arrayListStudentPassHistory[position].passData2

            when(arrayListStudentPassHistory[position].isRectorAccepted){
                0 -> {
                    holder.textViewRectorIsAccepted.text = view.getString(R.string.pending)
                }
                1 -> {
                    holder.textViewRectorIsAccepted.text = view.getString(R.string.approved)
                }
                else->{
                    holder.textViewRectorIsAccepted.text = view.getString(R.string.canceled)
                }
            }

            when(arrayListStudentPassHistory[position].isFacultyAccepted){
                0 -> {
                    holder.textViewFacultyIsAccepted.text = view.getString(R.string.pending)
                }
                1 -> {
                    holder.textViewFacultyIsAccepted.text = view.getString(R.string.approved)
                }
                else->{
                    holder.textViewFacultyIsAccepted.text = view.getString(R.string.canceled)
                }
            }
        }else{
            holder.textViewLeaveType.text = view.getString(R.string.late_pass)
            holder.textViewStartDateText.text = view.getString(R.string.going_date)
            holder.textViewEndDateText.text = view.getString(R.string.going_time)
            holder.textViewRectorText.text = view.getString(R.string.rector)
            holder.textViewFacultyText.text = ""
            holder.textViewFacultyIsAccepted.text = ""
            /////////////////////////
            holder.textViewStartDate.text = arrayListStudentPassHistory[position].passData1
            holder.textViewEndDate.text = arrayListStudentPassHistory[position].passData2

            when(arrayListStudentPassHistory[position].isRectorAccepted){
                0 -> {
                    holder.textViewRectorIsAccepted.text = view.getString(R.string.pending)
                }
                1 -> {
                    holder.textViewRectorIsAccepted.text = view.getString(R.string.approved)
                }
                else->{
                    holder.textViewRectorIsAccepted.text = view.getString(R.string.canceled)
                }
            }
        }

        holder.buttonCancelPass.setOnClickListener {
            /*holder.groupButtonActions.visibility = View.GONE
            holder.textViewYourPassIsCanceled.visibility = View.VISIBLE*/

            val client = OkHttpClient()
            val request = Request.Builder()
                    .url("${MainClass().getURL()}studentCancelPass.php?student_id=${arrayListStudentPassHistory[position].studentID}&pass_id=${arrayListStudentPassHistory[position].passID}")
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    activity.runOnUiThread {
                        val data = response.body?.string() as String
                        Log.d("Data", data)
                        val JsonObject = JSONObject(data)
                        val success = JsonObject.getInt("success")
                        if (success == 1) {
                            holder.groupButtonActions.visibility = View.GONE
                            holder.textViewYourPassIsCanceled.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }

        holder.buttonViewPass.setOnClickListener {
            val alert = AlertDialog.Builder(it.context)
            val layoutInflater = LayoutInflater.from(it.context)
            val view = layoutInflater.inflate(R.layout.custom_view_pass_design, null)

            val textViewPurpose = view.findViewById<AppCompatTextView>(R.id.textViewPurpose)
            val textLeaveType = view.findViewById<AppCompatTextView>(R.id.textHostelNo)
            val textViewApproved = view.findViewById<AppCompatTextView>(R.id.textViewApproved)
            val textViewStartDateText = view.findViewById<AppCompatTextView>(R.id.textViewStartDateText)
            val textViewEndDateText = view.findViewById<AppCompatTextView>(R.id.textViewEndDateText)
            val textViewStartDate = view.findViewById<AppCompatTextView>(R.id.textViewStartDate)
            val textViewEndDate = view.findViewById<AppCompatTextView>(R.id.textViewEndDate)
            val textViewDestination = view.findViewById<AppCompatTextView>(R.id.textViewDestination)
            textViewPurpose.movementMethod = ScrollingMovementMethod()


            if(arrayListStudentPassHistory[position].leaveType == 1){ //Late Pass
                textLeaveType.text = "LATE PASS"
                if (arrayListStudentPassHistory[position].isRectorAccepted == 1) {
                    textViewApproved.text = "Accepted"
                    textViewApproved.isSelected = true
                } else if (arrayListStudentPassHistory[position].isRectorAccepted == 2) {
                    textViewApproved.text = "Canceled"
                    textViewApproved.isSelected = false
                } else if (arrayListStudentPassHistory[position].isRectorAccepted == 0) {
                    textViewApproved.text = "Pending"
                    textViewApproved.isSelected = false
                }

                textViewStartDateText.text = "Going Date"
                textViewEndDateText.text = "Going Time"

                textViewStartDate.text = arrayListStudentPassHistory[position].passData1
                textViewEndDate.text = arrayListStudentPassHistory[position].passData2

                textViewDestination.text = arrayListStudentPassHistory[position].passDestination
                textViewPurpose.text = arrayListStudentPassHistory[position].passPurpose
            }else{
                textLeaveType.text = "HOME PASS"
                if (arrayListStudentPassHistory[position].isRectorAccepted == 1 && arrayListStudentPassHistory[position].isFacultyAccepted == 1) {
                    textViewApproved.text = "Accepted"
                    textViewApproved.isSelected = true
                } else if (arrayListStudentPassHistory[position].isRectorAccepted == 1 && arrayListStudentPassHistory[position].isFacultyAccepted == 0) {
                    textViewApproved.text = "Pending"
                    textViewApproved.isSelected = false
                } else if (arrayListStudentPassHistory[position].isRectorAccepted == 2 && arrayListStudentPassHistory[position].isFacultyAccepted == 2) {
                    textViewApproved.text = "Canceled"
                    textViewApproved.isSelected = false
                } else if (arrayListStudentPassHistory[position].isRectorAccepted == 0 && arrayListStudentPassHistory[position].isFacultyAccepted == 1) {
                    textViewApproved.text = "Pending"
                    textViewApproved.isSelected = false
                }

                textViewStartDateText.text = "Start Date"
                textViewEndDateText.text = "End Time"

                textViewStartDate.text = arrayListStudentPassHistory[position].passData1
                textViewEndDate.text = arrayListStudentPassHistory[position].passData2

                textViewDestination.text = arrayListStudentPassHistory[position].passDestination
                textViewPurpose.text = arrayListStudentPassHistory[position].passPurpose
            }




            val buttonDone = view.findViewById<Button>(R.id.buttonDone)
            alert.setView(view)
            val alertDialog = alert.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.show()
            buttonDone.setOnClickListener {
                alertDialog.dismiss()
            }

        }

    }

    override fun getItemCount(): Int {
        return arrayListStudentPassHistory.size
    }

}