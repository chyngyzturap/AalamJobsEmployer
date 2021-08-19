package com.pharos.aalamjobsemployer.data.responses

import com.pharos.aalamjobsemployer.data.model.Talents

data class TalentsResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<Talents>
)