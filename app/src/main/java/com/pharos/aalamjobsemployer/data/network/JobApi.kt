package com.pharos.aalamjobsemployer.data.network

import com.pharos.aalamjobsemployer.data.local.db.cv.models.*
import com.pharos.aalamjobsemployer.data.responses.*
import com.pharos.aalamjobsemployer.data.responses.dialog.*
import retrofit2.http.*

interface JobApi {
    @POST("/api/jobs/")
    suspend fun createJob(@Body jobModel: JobModel): JobModelResponse

    @POST("/api/organizations/")
    suspend fun createCompany(@Body companyModel: CompanyModel): CompanyModelResponse

    @GET("/api/jobs/me/")
    suspend fun getJobs(
        @Query("page") page: Int
    ): JobModelResponse

    @GET("/api/countries/")
    suspend fun getCountries(): CountryResponse

    @GET("/api/cities/")
    suspend fun getCities(): CityResponse

    @GET("/api/specializations/")
    suspend fun getSpec(): SectorResponse

    @GET("/api/currencies/")
    suspend fun getCurrencies(): CurrencyResponse

    @GET("/api/types/employment/")
    suspend fun getEmploymentTypes(): EmploymentTypeResponse

    @GET("/api/types/payment/")
    suspend fun getPaymentTypes(): PaymentTypeResponse

    @GET("/api/organizations/")
    suspend fun getOrganizations(): OrganizationResponse

    @GET("/api/organizations/{id}")
    suspend fun getMyOrganization(
        @Path("id") id: Int
        ): CompanyModelResponse

    @GET("/api/jobs/{id}/")
    suspend fun getJobById(
        @Path("id") storeId: Int
    ): JobModelResponseItem

    @GET("/api/users/recruiters/me/")
    suspend fun getUserData(): UserResponse
}