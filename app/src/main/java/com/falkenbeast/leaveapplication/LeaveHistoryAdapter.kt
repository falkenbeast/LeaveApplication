package com.falkenbeast.leaveapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LeaveHistoryAdapter(private val leaveList: List<LeaveApplication>) :
    RecyclerView.Adapter<LeaveHistoryAdapter.LeaveViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leave_history, parent, false)
        return LeaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        val leave = leaveList[position]
        holder.tvLeaveReason.text = leave.reason
        holder.tvLeaveDates.text = "${leave.startDate} - ${leave.endDate}"
        holder.tvLeaveStatus.text = "Status: ${leave.status}"

        // Change status color dynamically
        when (leave.status) {
            "Approved" -> holder.tvLeaveStatus.setTextColor(0xFF4CAF50.toInt()) // Green
            "Rejected" -> holder.tvLeaveStatus.setTextColor(0xFFF44336.toInt()) // Red
            else -> holder.tvLeaveStatus.setTextColor(0xFFFF9800.toInt()) // Orange
        }
    }

    override fun getItemCount(): Int = leaveList.size

    class LeaveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLeaveReason: TextView = view.findViewById(R.id.tvLeaveReason)
        val tvLeaveDates: TextView = view.findViewById(R.id.tvLeaveDates)
        val tvLeaveStatus: TextView = view.findViewById(R.id.tvLeaveStatus)
    }
}
