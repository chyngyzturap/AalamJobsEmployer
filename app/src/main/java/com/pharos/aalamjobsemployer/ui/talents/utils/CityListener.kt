package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.responses.dialog.CityResponse


interface CityListener {
    fun setCities(jobs: CityResponse)
    fun getCityError(code: Int?)
}