package com.pharos.aalamjobsemployer.data.repository

import com.pharos.aalamjobsemployer.data.local.prefs.UserPreferences
import com.pharos.aalamjobsemployer.data.model.ChangePasswordModel
import com.pharos.aalamjobsemployer.data.model.CreateUserModel
import com.pharos.aalamjobsemployer.data.model.ForgotPasswordModel
import com.pharos.aalamjobsemployer.data.model.TokenObtainPair
import com.pharos.aalamjobsemployer.data.network.AuthApi
import okhttp3.Address
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepository(
    private val api: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun login(tokenObtainPair: TokenObtainPair) = safeApiCall {
        api.login(tokenObtainPair)
    }

    suspend fun createUser(createUserModel: CreateUserModel) = safeApiCall {
        api.createUser(createUserModel)
    }

    suspend fun changePassword(changePasswordModel: ChangePasswordModel) = safeApiCall {
        api.changePassword(changePasswordModel)
    }

    suspend fun forgotPassword(forgotPasswordModel: ForgotPasswordModel) = safeApiCall {
        api.forgotPassword(forgotPasswordModel)
    }

    suspend fun updateProfile(
        photo: MultipartBody.Part?, email: RequestBody, fullname: RequestBody,
        position: RequestBody, city: RequestBody, country: RequestBody, address: RequestBody,
        organization: RequestBody
    ) = safeApiCall {
        api.updateProfile(photo, email, fullname, position, city, country, address, organization)
    }

    suspend fun getProfileInfo() = safeApiCall {
        api.getProfileInfo()
    }

    suspend fun checkPhone(phone: String) = safeApiCall {
        api.checkPhone(phone)
    }

    suspend fun saveAuthToken(tokenAccess: String, tokenRefresh: String) {
        preferences.saveAuthToken(tokenAccess, tokenRefresh)
    }

    suspend fun saveLang(lang: String) {
        preferences.saveLang(lang)
    }

    suspend fun saveUser(
        email: String, id: Int, username: String, photo: String,
        city: String, country: String, position: String, fullname: String
    ) {
        preferences.saveUser(email, id, username, photo, city, country, position, fullname)
    }

    suspend fun logout() {
        preferences.clear()
    }

}