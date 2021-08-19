package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.responses.dialog.CountryResponse


interface CountryListener {
    fun setCountry(jobs: CountryResponse)
    fun getCountryError(code: Int?)
}