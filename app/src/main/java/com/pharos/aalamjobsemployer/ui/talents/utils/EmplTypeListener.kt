package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.responses.dialog.EmploymentTypeResponse


interface EmplTypeListener {
    fun setEmplTypes(emplTypes: EmploymentTypeResponse)
    fun getEmplTypesError(code: Int?)
}