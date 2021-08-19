package com.pharos.aalamjobsemployer.data.model

data class Country(
    val country_code: String,
    val id: Int,
    val is_active: Boolean,
    val name: CountryName
)