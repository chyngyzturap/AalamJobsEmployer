package com.pharos.aalamjobsemployer.data.network

import com.pharos.aalamjobsemployer.data.model.ChangePasswordModel
import com.pharos.aalamjobsemployer.data.model.CreateUserModel
import com.pharos.aalamjobsemployer.data.model.ForgotPasswordModel
import com.pharos.aalamjobsemployer.data.model.TokenObtainPair
import com.pharos.aalamjobsemployer.data.responses.LoginResponse
import com.pharos.aalamjobsemployer.data.responses.PhoneCheckResponse
import com.pharos.aalamjobsemployer.data.responses.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface AuthApi {

    @POST("/api/auth/jwt/create/")
    suspend fun login(@Body tokenObtainPair: TokenObtainPair) : LoginResponse

    @POST("/api/auth/users/")
    suspend fun createUser(@Body createUserModel: CreateUserModel): LoginResponse

    @PATCH("/api/users/recruiters/me/")
    @Multipart
    suspend fun updateProfile(
        @Part photo: MultipartBody.Part?,
        @Part("email") email: RequestBody?,
        @Part("fullname") fullname: RequestBody?,
        @Part("position") position: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("country") country: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("organization") organization: RequestBody?
    ): UserResponse

    @GET("/api/users/recruiters/me/")
    suspend fun getProfileInfo(): UserResponse

    @POST("/api/auth/users/set_password/")
    suspend fun changePassword(@Body changePasswordModel: ChangePasswordModel)

    @POST("/api/users/reset/password/")
    suspend fun forgotPassword(@Body forgotPasswordModel: ForgotPasswordModel)

    @FormUrlEncoded
    @POST("/api/phone/check/")
    suspend fun checkPhone(@Field("phone") phone: String): PhoneCheckResponse

    @POST("logout")
    suspend fun logout(): ResponseBody
}