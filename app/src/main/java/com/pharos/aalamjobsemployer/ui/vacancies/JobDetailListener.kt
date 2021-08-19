package com.pharos.aalamjobsemployer.ui.vacancies

import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponse
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponseItem

interface JobDetailListener {
    fun setJobDetail(jobDetail: JobModelResponseItem)
    fun getJobError(code: Int?)
}