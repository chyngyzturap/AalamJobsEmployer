package com.pharos.aalamjobsemployer.ui.talents.model

data class Owner(
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
    val password: String,
    val photo: String,
    val role: String,
    val role_text: String,
    val user_permissions: List<Int>,
    val username: String
)