package com.pharos.aalamjobsemployer.data.local.db.cv.models

data class JobModelResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<JobModelResponseItem>
)