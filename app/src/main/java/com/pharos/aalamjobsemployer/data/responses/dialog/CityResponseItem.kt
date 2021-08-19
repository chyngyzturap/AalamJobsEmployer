package com.pharos.aalamjobsemployer.data.responses.dialog

data class CityResponseItem(
    val country: Int,
    val country_name: Name,
    val created_at: String,
    val id: Int,
    val latitude: Int,
    val locale_name: String,
    val longitude: Int,
    val name: Name,
    val updated_at: String
)