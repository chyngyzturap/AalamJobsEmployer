package com.pharos.aalamjobsemployer.ui.favorites.model

data class Resume(
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
    val photo: String,
    val position: String,
    val profile: String,
    val salary: Int,
    val updated_at: String,
    val favorite: Boolean
)