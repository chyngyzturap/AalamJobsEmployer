package com.pharos.aalamjobsemployer.data.responses

import com.pharos.aalamjobsemployer.data.model.FavTalents

data class FavJobsResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<FavTalents>
)