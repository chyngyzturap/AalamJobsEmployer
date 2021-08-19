package com.pharos.aalamjobsemployer.data.model

data class CreateUserModel(
    val username: String?,
    val password: String?,
    val role: String?,
    val fullname: String?
)