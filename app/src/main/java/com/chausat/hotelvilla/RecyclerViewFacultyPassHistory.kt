package com.chausat.hotelvilla

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RecyclerViewFacultyPassHistory(
    var arrayListRectorPassHistory: ArrayList<RectorPassHistory>,
    val facultyPassActivity: FacultyPassActivity
) : RecyclerView.Adapter<RecyclerViewFacultyPassHistory.ViewHolderRectorPassHistory>() {

    lateinit var view: Context

    inner class ViewHolderRectorPassHistory(view: View) : RecyclerView.ViewHolder(view) {
        val textViewStudentName = view.findViewById<TextView>(R.id.textViewStudentName)
        val textViewLeaveType = view.findViewById<TextView>(R.id.textViewLeaveType)
        val textViewLeaveData1Text = view.findViewById<TextView>(R.id.textViewLeaveData1Text)
        val textViewData1 = view.findViewById<TextView>(R.id.textViewData1)
        val textViewLeaveData2Text = view.findViewById<TextView>(R.id.textViewLeaveData2Text)
        val textViewData2 = view.findViewById<TextView>(R.id.textViewData2)
        val textViewRector = view.findViewById<TextView>(R.id.textViewRector)
        val textViewFaculty = view.findViewById<TextView>(R.id.textViewFaculty)

        val buttonCancelPass = view.findViewById<MaterialButton>(R.id.buttonCancelPass)
        val buttonAcceptPass = view.findViewById<MaterialButton>(R.id.buttonAcceptPass)
        val buttonViewPass = view.findViewById<MaterialButton>(R.id.buttonViewPass)
        val textViewYourPassIsCanceled =
            view.findViewById<TextView>(R.id.textViewYourPassIsCanceled)

        val groupButtonActions = view.findViewById<Group>(R.id.groupButtonActions)
        val groupButtonMain = view.findViewById<Group>(R.id.groupButtonMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRectorPassHistory {
        view = parent.context
        return ViewHolderRectorPassHistory(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rector_pass_history_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderRectorPassHistory, position: Int) {
        if (arrayListRectorPassHistory[position].isCanceledAccepted == 1) {
            holder.textViewYourPassIsCanceled.text =
                holder.itemView.context.getString(R.string.label_cancel)
            holder.textViewYourPassIsCanceled.visibility = View.VISIBLE
            holder.groupButtonActions.visibility = View.GONE
            holder.groupButtonMain.visibility = View.GONE
        }

        when {
            arrayListRectorPassHistory[position].isFacultyAccepted == 1 -> {
                holder.groupButtonMain.visibility = View.GONE
                holder.textViewFaculty.text = view.getString(R.string.approved)
            }
            arrayListRectorPassHistory[position].isFacultyAccepted == 2 -> {
                holder.groupButtonMain.visibility = View.GONE
                holder.textViewFaculty.text = view.getString(R.string.canceled)
            }
            arrayListRectorPassHistory[position].isCanceledAccepted != 1 -> {
                holder.groupButtonMain.visibility = View.VISIBLE
            }
        }

        if (arrayListRectorPassHistory[position].passType == 0) {
            holder.textViewStudentName.text = arrayListRectorPassHistory[position].studentName
            holder.textViewLeaveType.text = view.getString(R.string.home_pass)
            holder.textViewLeaveData1Text.text = view.getString(R.string.start_date)
            holder.textViewLeaveData2Text.text = view.getString(R.string.end_date)

            holder.textViewData1.text = arrayListRectorPassHistory[position].data1
            holder.textViewData2.text = arrayListRectorPassHistory[position].data2

            when (arrayListRectorPassHistory[position].isRectorAccepted) {
                0 -> {
                    holder.textViewRector.text = view.getString(R.string.pending)
                }
                1 -> {
                    holder.textViewRector.text = view.getString(R.string.approved)
                }
                else -> {
                    holder.textViewRector.text = view.getString(R.string.canceled)
                }
            }

            when (arrayListRectorPassHistory[position].isFacultyAccepted) {
                0 -> {
                    holder.textViewFaculty.text = view.getString(R.string.pending)
                }
                1 -> {
                    holder.textViewFaculty.text = view.getString(R.string.approved)
                }
                else -> {
                    holder.textViewFaculty.text = view.getString(R.string.canceled)
                }
            }
        }

        holder.buttonViewPass.setOnClickListener {
            val alert = AlertDialog.Builder(it.context)
            val layoutInflater = LayoutInflater.from(it.context)
            val view = layoutInflater.inflate(R.layout.custom_view_pass_design, null)


            val textLeaveType = view.findViewById<AppCompatTextView>(R.id.textHostelNo)
            val textViewApproved = view.findViewById<AppCompatTextView>(R.id.textViewApproved)
            val textViewStartDateText =
                view.findViewById<AppCompatTextView>(R.id.textViewStartDateText)
            val textViewEndDateText = view.findViewById<AppCompatTextView>(R.id.textViewEndDateText)
            val textViewStartDate = view.findViewById<AppCompatTextView>(R.id.textViewStartDate)
            val textViewEndDate = view.findViewById<AppCompatTextView>(R.id.textViewEndDate)
            val textViewDestination = view.findViewById<AppCompatTextView>(R.id.textViewDestination)
            val textViewPurpose = view.findViewById<TextView>(R.id.textViewPurpose)
            textViewPurpose.movementMethod = ScrollingMovementMethod()

            textLeaveType.text = "HOME PASS"
            if (arrayListRectorPassHistory[position].isRectorAccepted == 1 && arrayListRectorPassHistory[position].isFacultyAccepted == 1) {
                textViewApproved.text = "Accepted"
                textViewApproved.isSelected = true
            } else if (arrayListRectorPassHistory[position].isRectorAccepted == 1 && arrayListRectorPassHistory[position].isFacultyAccepted == 0) {
                textViewApproved.text = "Pending"
                textViewApproved.isSelected = false
            } else if (arrayListRectorPassHistory[position].isRectorAccepted == 2 && arrayListRectorPassHistory[position].isFacultyAccepted == 2) {
                textViewApproved.text = "Canceled"
                textViewApproved.isSelected = false
            } else if (arrayListRectorPassHistory[position].isRectorAccepted == 0 && arrayListRectorPassHistory[position].isFacultyAccepted == 1) {
                textViewApproved.text = "Pending"
                textViewApproved.isSelected = false
            }

            textViewStartDateText.text = "Start Date"
            textViewEndDateText.text = "End Time"
            textViewStartDate.text = arrayListRectorPassHistory[position].data1
            textViewEndDate.text = arrayListRectorPassHistory[position].data1
            textViewDestination.text = arrayListRectorPassHistory[position].destination
            textViewPurpose.text = arrayListRectorPassHistory[position].purpose

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

        holder.buttonCancelPass.setOnClickListener {
            setApproved(
                arrayListRectorPassHistory[position].studentId,
                arrayListRectorPassHistory[position].Id,
                2,
                holder
            )
        }

        holder.buttonAcceptPass.setOnClickListener {
            setApproved(
                arrayListRectorPassHistory[position].studentId,
                arrayListRectorPassHistory[position].Id,
                1,
                holder
            )
        }

    }

    private fun setApproved(
        studentId: Int,
        id: Int,
        isApproved: Int,
        holder: ViewHolderRectorPassHistory
    ) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${MainClass().getURL()}facultyApproveUpdate.php?student_id=$studentId&pass_id=$id&isApproved=$isApproved")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                facultyPassActivity.runOnUiThread {
                    val dialog = Dialog(facultyPassActivity)
                    dialog.setContentView(R.layout.custom_loading)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show()
                    val data = response.body?.string() as String
                    Log.d("Data", data)
                    val JsonObject = JSONObject(data)
                    val success = JsonObject.getInt("success")
                    if (success == 1) {
                        holder.groupButtonMain.visibility = View.GONE
                        if (isApproved == 1) {
                            holder.textViewFaculty.text = view.getString(R.string.approved)
                        } else {
                            holder.textViewFaculty.text = view.getString(R.string.canceled)
                        }
                    }
                    dialog.dismiss()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return arrayListRectorPassHistory.size
    }

}