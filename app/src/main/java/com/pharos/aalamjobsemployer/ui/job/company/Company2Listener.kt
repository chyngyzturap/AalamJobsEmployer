package com.pharos.aalamjobsemployer.ui.job.company

import com.pharos.aalamjobsemployer.data.local.db.cv.models.CompanyModelResponse
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponse
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponseItem

interface Company2Listener {
    fun setCompany(company: CompanyModelResponse)
    fun getCompanyError(code: Int?)
}