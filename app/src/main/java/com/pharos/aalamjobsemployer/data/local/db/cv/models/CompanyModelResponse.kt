package com.pharos.aalamjobsemployer.data.local.db.cv.models

import com.pharos.aalamjobsemployer.ui.talents.model.Country
import com.pharos.aalamjobsemployer.ui.talents.model.Sector

data class CompanyModelResponse(
    val about: String,
    val address: String,
    val country: Country,
    val created_at: String,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val sectors: List<Sector>,
    val staff_amount: Int,
    val updated_at: String,
    val website: String
)