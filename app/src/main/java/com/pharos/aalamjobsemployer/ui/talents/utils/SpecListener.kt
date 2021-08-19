package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.responses.dialog.SectorResponse


interface SpecListener {
    fun setSpec(jobs: SectorResponse)
    fun getSpecError(code: Int?)
}