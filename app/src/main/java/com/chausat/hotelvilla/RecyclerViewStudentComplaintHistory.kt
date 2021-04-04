package com.chausat.hotelvilla

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewStudentComplaintHistory(var arrayListComplaint: ArrayList<ComplaintInformation>, val activity: FragmentActivity)
    : RecyclerView.Adapter<RecyclerViewStudentComplaintHistory.ViewHolderComplaint>(){

    inner class ViewHolderComplaint(view : View) : RecyclerView.ViewHolder(view){
        val textHostelNo = view.findViewById<TextView>(R.id.textHostelNo)
        val textViewRoomNo = view.findViewById<TextView>(R.id.textViewRoomNo)
        val textHostelSubject = view.findViewById<TextView>(R.id.textHostelSubject)
        val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
        val textViewIsComplaintSeen = view.findViewById<TextView>(R.id.textViewIsComplaintSeen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderComplaint {
        return ViewHolderComplaint(
            LayoutInflater.from(parent.context).inflate(R.layout.student_complaint_history_recyclerview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderComplaint, position: Int) {
        holder.textHostelNo.text = arrayListComplaint[position].hostelNo.toString()
        holder.textViewRoomNo.text = arrayListComplaint[position].roomNo.toString()
        holder.textHostelSubject.text = arrayListComplaint[position].subject
        holder.textViewDescription.text = arrayListComplaint[position].description

        if(arrayListComplaint[position].isSeen == 1){
            holder.textViewIsComplaintSeen.isSelected = true
            holder.textViewIsComplaintSeen.text = "seen"
        }
    }

    override fun getItemCount(): Int {
        return arrayListComplaint.size
    }

}