package com.pharos.aalamjobsemployer.data.repository

import com.pharos.aalamjobsemployer.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobsemployer.data.local.db.cv.models.CompanyModel
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModel
import com.pharos.aalamjobsemployer.data.local.prefs.UserPreferences
import com.pharos.aalamjobsemployer.data.network.JobApi


class JobRepository(
    private val api: JobApi
) : BaseRepository() {
    private val cvDatabase: CvDatabase? = null
    private val preferences: UserPreferences? = null

    suspend fun createJob(jobModel: JobModel) = safeApiCall {
        api.createJob(jobModel)
    }

    suspend fun createNewCompany(companyModel: CompanyModel) = safeApiCall {
        api.createCompany(companyModel)
    }

    suspend fun companyCreated(created: String) {
        preferences?.companyCreated(created)
    }

    suspend fun getJobById(jobId: Int) = safeApiCall {
        api.getJobById(jobId)
    }

    suspend fun getMyJobs(page: Int) = safeApiCall {
        api.getJobs(page)
    }

    suspend fun getCountries() = safeApiCall {
        api.getCountries()
    }

    suspend fun getCities() = safeApiCall {
        api.getCities()
    }

    suspend fun getSpec() = safeApiCall {
        api.getSpec()
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

    suspend fun getOrganizations() = safeApiCall {
        api.getOrganizations()
    }

    suspend fun getMyOrganization(id: Int) = safeApiCall {
        api.getMyOrganization(id)
    }

    suspend fun getUserData() = safeApiCall {
        api.getUserData()
    }


}