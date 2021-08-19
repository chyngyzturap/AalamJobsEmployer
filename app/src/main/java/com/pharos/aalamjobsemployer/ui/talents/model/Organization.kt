package com.pharos.aalamjobsemployer.ui.talents.model

data class Organization(
    val address: String,
    val country: com.pharos.aalamjobsemployer.ui.talents.model.Country,
    val id: Int,
    val name: String,
    val phone: String,
    val sectors: List<Sector>
)