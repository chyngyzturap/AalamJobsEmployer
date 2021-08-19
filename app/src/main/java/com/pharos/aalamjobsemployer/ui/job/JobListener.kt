package com.pharos.aalamjobsemployer.ui.job

import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponse
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponseItem

interface JobListener {
    fun createCvSuccess()
    fun createCvFailed(code: Int?)

    fun setResume(job: JobModelResponse)
    fun getCvError(code: Int?)

    fun setJobDetail(jobDetail: JobModelResponseItem)
    fun getJobError(code: Int?)
}