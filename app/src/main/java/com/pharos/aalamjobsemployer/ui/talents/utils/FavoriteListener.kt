package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.model.FavTalents
import com.pharos.aalamjobsemployer.data.responses.FavJobsResponse
import com.pharos.aalamjobsemployer.ui.favorites.model.FavoriteTalents
import com.pharos.aalamjobsemployer.ui.favorites.model.FavoriteTalentsItem


interface FavoriteListener {
    fun postFavJobSuccess()
    fun addToFavFailed(code: Int?)
    fun deleteFromFav()
    fun setFavoriteJob(jobs: FavoriteTalents)
    fun getFavJobError(code: Int?)
}