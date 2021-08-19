package com.pharos.aalamjobsemployer.data.responses

import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponse

data class JobResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<JobModelResponse>
)