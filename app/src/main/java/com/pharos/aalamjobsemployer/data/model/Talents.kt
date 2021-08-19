package com.pharos.aalamjobsemployer.data.model

import com.pharos.aalamjobsemployer.ui.talents.model.Currency

data class Talents(
    val favorite: Boolean,
    val photo: String,
    val achievements: List<String>,
    val bio: String,
    val birthdate: String,
    val citizenship: String,
    val created_at: String,
    val current_city: String,
    val current_country: String,
    val date_can_start: String,
    val email: String,
    val firstname: String,
    val gender: String,
    val has_computer: Boolean,
    val id: Int,
    val lastname: String,
    val living_address: String,
    val locale: String,
    val marital_status: String,
    val middlename: String,
    val mother_language: String,
    val phone: String,
    val portfolio: List<String>,
    val position: String,
    val profile: String,
    val salary: Int,
    val currency: Currency,
    val skills: List<String>,
    val updated_at: String
)