package com.pharos.aalamjobsemployer.data.local.db.cv.models

import com.pharos.aalamjobsemployer.data.model.City
import com.pharos.aalamjobsemployer.ui.talents.model.*
import com.pharos.aalamjobsemployer.ui.talents.model.Salary

data class JobModelResponseItem(
    val city: City,
    val deadline: String,
    val deadline_string: String,
    val description: String,
    val extra_benefits: String,
    val id: Int,
    val owner: Owner,
    val position: String,
    val published: String,
    val published_date: String,
    val requirements: List<String>,
    val responsibilities: List<String>,
    val salary: Salary,
    val currency: Currency,
    val schedule: String,
    val start_date: String,
    val title: String,
    val updated: String,
    val updated_date: String,
    val organization: Organization,
    var favorite: Boolean
)