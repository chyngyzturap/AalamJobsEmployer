package com.pharos.aalamjobsemployer.data.model

data class City(
    val country: Country,
    val id: Int,
    val latitude: Any,
    val longitude: Any,
    val name: CityName
)