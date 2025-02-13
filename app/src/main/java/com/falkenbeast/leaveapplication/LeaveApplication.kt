package com.falkenbeast.leaveapplication

data class LeaveApplication(
    val startDate: String,
    val endDate: String,
    val reason: String,
    val status: String
)
