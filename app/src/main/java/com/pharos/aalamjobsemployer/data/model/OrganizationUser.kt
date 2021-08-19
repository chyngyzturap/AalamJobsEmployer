package com.pharos.aalamjobsemployer.data.model

data class OrganizationUser(
    val about: String,
    val address: String,
    val country: Int,
    val created_at: String,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val sectors: List<Int>,
    val staff_amount: Int,
    val updated_at: String,
    val website: String
)