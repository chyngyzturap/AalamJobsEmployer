package com.pharos.aalamjobsemployer.ui.favorites.model

data class User(
    val address: String,
    val city: String,
    val country: String,
    val date_joined: String,
    val email: String,
    val first_name: String,
    val fullname: String,
    val groups: List<Int>,
    val id: Int,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_login: String,
    val last_name: String,
    val organization: Int,
    val password: String,
    val phone: String,
    val photo: String,
    val position: String,
    val role: String,
    val user_permissions: List<Int>,
    val username: String
)