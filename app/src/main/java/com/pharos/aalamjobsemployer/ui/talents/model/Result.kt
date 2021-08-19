package com.pharos.aalamjobsemployer.ui.talents.model

data class Result(
    val city: Int,
    val deadline: String,
    val deadline_string: String,
    val description: String,
    val employment_type: Int,
    val extra_benefits: String,
    val id: Int,
    val owner: Int,
    val payment_type: Int,
    val position: String,
    val published: String,
    val published_date: String,
    val requirements: List<String>,
    val responsibilities: List<String>,
    val salary: Salary,
    val schedule: String,
    val start_date: String,
    val title: String,
    val updated: String,
    val updated_date: String,
    val organization: Organization
)