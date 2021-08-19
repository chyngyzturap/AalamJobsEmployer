package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.responses.TalentsResponse


interface TalentsListener {
    fun setTalent(talents: TalentsResponse)
    fun getTalentError(code: Int?)
}