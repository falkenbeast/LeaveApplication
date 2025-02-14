package com.falkenbeast.leaveapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class LeaveApplicationActivity : AppCompatActivity() {

    private lateinit var etReason: EditText
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnHistory: Button
    private lateinit var btnLogout: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid ?: "testUser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_application)

        etReason = findViewById(R.id.etReason)
        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnHistory = findViewById(R.id.btnHistory)
        btnLogout = findViewById(R.id.btnLogout)

        btnSubmit.setOnClickListener { submitApplication() }
        btnHistory.setOnClickListener {
            startActivity(Intent(this, ApplicationHistoryActivity::class.java))
        }
        btnLogout.setOnClickListener { logoutUser() }

        // Date Picker Setup
        etStartDate.setOnClickListener { showDatePicker(etStartDate) }
        etEndDate.setOnClickListener { showDatePicker(etEndDate) }

        // Disable manual input
        etStartDate.keyListener = null
        etEndDate.keyListener = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(formattedDate)
        }, year, month, day)

        datePicker.show()
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(0xFFFFFFFF.toInt())
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(0xFFFFFFFF.toInt())
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
        auth.signOut()
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}
