package com.pharos.aalamjobsemployer.data.local.db.cv.models

import com.pharos.aalamjobsemployer.ui.talents.model.Salary

data class JobModel(
    val city: Int,
    val currency: Int,
    val deadline: String,
    val description: String,
    val employment_type: Int,
    val organization: Int,
    val payment_type: Int,
    val position: String,
    val requirements: List<String>,
    val responsibilities: List<String>,
    val salary: Salary,
    val schedule: String,
    val specialization: Int,
    val start_date: String,
    val title: String




)