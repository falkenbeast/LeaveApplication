package com.falkenbeast.leaveapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LeaveApplicationActivity : AppCompatActivity() {

    private lateinit var etReason: EditText
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnHistory: Button
    private lateinit var btnLogout: Button // Added logout button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance() // Fixed issue
    private val userId = auth.currentUser?.uid ?: "testUser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_application)

        etReason = findViewById(R.id.etReason)
        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnHistory = findViewById(R.id.btnHistory)
        btnLogout = findViewById(R.id.btnLogout) // Initialize logout button

        btnSubmit.setOnClickListener { submitApplication() }
        btnHistory.setOnClickListener {
            startActivity(Intent(this, ApplicationHistoryActivity::class.java))
        }
        btnLogout.setOnClickListener { logoutUser() } // Added click listener for logout
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun submitApplication() {
        val reason = etReason.text.toString().trim()
        val startDate = etStartDate.text.toString().trim()
        val endDate = etEndDate.text.toString().trim()

        if (startDate.isEmpty() || endDate.isEmpty() || reason.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val leaveData = hashMapOf(
            "startDate" to startDate,
            "endDate" to endDate,
            "reason" to reason,
            "status" to "Pending"
        )

        db.collection("LeaveApplications")
            .document(userId)
            .collection("applications")
            .add(leaveData)
            .addOnSuccessListener {
                Toast.makeText(this, "Leave Applied!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ApplicationHistoryActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun logoutUser() {
        auth.signOut() // Fixed crash issue
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, SignInActivity ::class.java))
        finish()
    }
}
