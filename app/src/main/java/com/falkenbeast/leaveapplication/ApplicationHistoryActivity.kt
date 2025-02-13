package com.falkenbeast.leaveapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ApplicationHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: LeaveHistoryAdapter
    private val historyList = mutableListOf<LeaveApplication>()

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "testUser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_history)

        recyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        historyAdapter = LeaveHistoryAdapter(historyList)
        recyclerView.adapter = historyAdapter

        fetchHistory()
    }

    private fun fetchHistory() {
        db.collection("LeaveApplications")
            .document(userId)
            .collection("applications")
            .get()
            .addOnSuccessListener { documents ->
                historyList.clear()
                for (document in documents) {
                    val startDate = document.getString("startDate") ?: "N/A"
                    val endDate = document.getString("endDate") ?: "N/A"
                    val reason = document.getString("reason") ?: "No Reason"
                    val status = document.getString("status") ?: "Pending"

                    historyList.add(LeaveApplication(startDate, endDate, reason, status))
                }
                historyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching history!", Toast.LENGTH_SHORT).show()
            }
    }
}
