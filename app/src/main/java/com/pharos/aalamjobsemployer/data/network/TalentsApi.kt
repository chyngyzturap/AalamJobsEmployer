package com.pharos.aalamjobsemployer.data.network

import com.pharos.aalamjobs.data.model.TokenAccess
import com.pharos.aalamjobs.data.model.TokenRefresh
import com.pharos.aalamjobsemployer.data.model.Talents
import com.pharos.aalamjobsemployer.data.responses.*
import com.pharos.aalamjobsemployer.data.responses.dialog.*
import com.pharos.aalamjobsemployer.ui.favorites.model.FavoriteTalents
import com.pharos.aalamjobsemployer.ui.talents.model.ResumeId
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TalentsApi {
    @POST("/api/auth/jwt/verify/")
    suspend fun verify(@Body verify: TokenAccess)
    @POST("/api/auth/jwt/refresh/")
    suspend fun refresh(@Body refresh: TokenRefresh) : TokenAccessResponse

    @GET("/api/resumes/")
    suspend fun getTalents(
        @Query ("search") q: String,
        @Query ("page") page: Int,
    ): TalentsResponse

    @GET("/api/resumes/")
    suspend fun getTalentsFiltered(
        @QueryMap options: Map<String, String>
        ): TalentsResponse

    @GET("/api/resumes/favorites/")
    suspend fun getFavoriteJobs(): FavoriteTalents

    @GET("/api/countries/")
    suspend fun getCountries(): CountryResponse

    @GET("/api/cities/")
    suspend fun getCities(): CityResponse

    @GET("/api/sectors/")
    suspend fun getSectors(): SectorResponse

    @GET("/api/currencies/")
    suspend fun getCurrencies(): CurrencyResponse

    @GET("/api/types/employment/")
    suspend fun getEmploymentTypes(): EmploymentTypeResponse

    @GET("/api/types/payment/")
    suspend fun getPaymentTypes(): PaymentTypeResponse

    @GET("/api/resumes/{id}/")
    suspend fun getResumesById(
        @Path("id") talentsId: Int
    ): Talents

    @POST("logout")
    suspend fun logout(): ResponseBody

    @POST("/api/resumes/favorites/")
    suspend fun setFavorite(
//        @Header("Authorization") token: String,
        @Body resumeId: ResumeId
    )

    @POST("/api/resumes/favorites/")
    suspend fun setFavoriteFromDetail(
        @Header("Authorization") token: String,
        @Body resumeId: ResumeId
    )

    @POST("/api/resumes/{id}/unfavorite/")
    suspend fun deleteFavorite(@Path("id") job: Int)

    @POST("/resumes/{id}/unfavorite/")
    suspend fun deleteFavoriteFromDetail(
        @Path("id") job: Int,
        @Header("Authorization") token: String,
    )

    companion object {

        private const val BASE_URL = "http://165.22.88.94:9000"

        operator fun invoke(): TalentsApi {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url
                    .newBuilder()
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TalentsApi::class.java)
        }
    }
}