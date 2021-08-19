package com.pharos.aalamjobsemployer.data.repository

import com.pharos.aalamjobs.data.model.TokenAccess
import com.pharos.aalamjobs.data.model.TokenRefresh
import com.pharos.aalamjobsemployer.data.local.prefs.UserPreferences
import com.pharos.aalamjobsemployer.data.network.TalentsApi
import com.pharos.aalamjobsemployer.ui.talents.model.ResumeId

class TalentsRepository (
    private val api: TalentsApi
    ) : BaseRepository() {
    private val preferences: UserPreferences? = null

    suspend fun saveAuthToken(tokenAccess: String) {
        preferences?.saveAccessToken(tokenAccess)
    }

    suspend fun verify(verify: TokenAccess) = safeApiCall {
        api.verify(verify)
    }
    suspend fun refresh(refresh: TokenRefresh) = safeApiCall {
        api.refresh(refresh)
    }


        suspend fun getTalents(page: Int, search: String) = safeApiCall {
            api.getTalents(search, page)
        }
        suspend fun getTalentsFiltered(options: Map<String, String>) = safeApiCall {
            api.getTalentsFiltered(options)
        }
        suspend fun getFavoriteJobs() = safeApiCall {
            api.getFavoriteJobs()
        }

        suspend fun getCountries() = safeApiCall {
            api.getCountries()
        }

        suspend fun getCities() = safeApiCall {
            api.getCities()
        }

        suspend fun getSectors() = safeApiCall {
            api.getSectors()
        }
    suspend fun getCurrencies() = safeApiCall {
        api.getCurrencies()
    }
    suspend fun getEmploymentTypes() = safeApiCall {
        api.getEmploymentTypes()
    }

    suspend fun getPaymentTypes() = safeApiCall {
        api.getPaymentTypes()
    }

    suspend fun getTalentById(jobId: Int) = safeApiCall {
        api.getResumesById(jobId)
    }

    suspend fun setFavorite(resumeId: ResumeId ) = safeApiCall {
        api.setFavorite(resumeId)
    }

    suspend fun setFavoriteFromDetail (resumeId: ResumeId, token: String) = safeApiCall {
        api.setFavoriteFromDetail(token, resumeId)
    }

    suspend fun deleteFavorite(jobId: Int) = safeApiCall {
        api.deleteFavorite(jobId)
    }

    suspend fun deleteFavoriteFromDetail(jobId: Int, token: String) = safeApiCall {
        api.deleteFavoriteFromDetail(jobId, token)
    }
}