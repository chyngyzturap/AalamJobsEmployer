package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.model.Talents

interface TalentListener {
    fun setJob(talents: Talents)
    fun getJobError(code: Int?)
}