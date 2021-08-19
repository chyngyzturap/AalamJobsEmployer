package com.pharos.aalamjobsemployer.data.responses.dialog

data class SpecResponseItem(
    val created_at: String,
    val id: Int,
    val name: String,
    val specializations: List<Specialization>,
    val updated_at: String
)