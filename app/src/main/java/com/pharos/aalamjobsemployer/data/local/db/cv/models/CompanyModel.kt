package com.pharos.aalamjobsemployer.data.local.db.cv.models

data class CompanyModel(
    val about: String,
    val address: String,
    val country: Int,
    val email: String,
    val name: String,
    val phone: String,
    val sectors: List<Int>,
    val website: String
)