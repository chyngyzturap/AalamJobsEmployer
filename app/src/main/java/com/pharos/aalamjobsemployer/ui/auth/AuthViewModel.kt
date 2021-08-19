package com.pharos.aalamjobsemployer.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobsemployer.data.model.ChangePasswordModel
import com.pharos.aalamjobsemployer.data.model.CreateUserModel
import com.pharos.aalamjobsemployer.data.model.ForgotPasswordModel
import com.pharos.aalamjobsemployer.data.model.TokenObtainPair
import com.pharos.aalamjobsemployer.data.network.Resource
import com.pharos.aalamjobsemployer.data.repository.AuthRepository
import com.pharos.aalamjobsemployer.data.responses.LoginResponse
import com.pharos.aalamjobsemployer.ui.auth.password.ResetPasswordListener
import com.pharos.aalamjobsemployer.ui.auth.utils.LoginListener
import com.pharos.aalamjobsemployer.ui.auth.register.RegisterListener
import com.pharos.aalamjobsemployer.ui.auth.utils.UserEditListener
import com.pharos.aalamjobsemployer.ui.auth.utils.UserListener
import com.pharos.aalamjobsemployer.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {
    private var registerListener: RegisterListener? = null
    private var loginListener: LoginListener? = null
    private var userListener: UserListener? = null
    private var userEditListener: UserEditListener? = null
    private var resetPwdListener: ResetPasswordListener? = null

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    fun login(
        tokenObtainPair: TokenObtainPair
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(tokenObtainPair)
    }

    fun createNewUser(
        createUserModel: CreateUserModel
    ) = viewModelScope.launch {
        when (val response = repository.createUser(createUserModel)) {
            is Resource.Success -> {
                registerListener?.createUserSuccess(response.value.username)
            }
            is Resource.Failure -> {
                loginListener?.signInFail(response.errorBody, response.errorCode)
                registerListener?.createUserFailed(response.errorCode)
            }
        }
    }

    fun checkPhone(
        phone: String
    ) = viewModelScope.launch {
        when (val response = repository.checkPhone(phone)) {
            is Resource.Success -> {
                loginListener?.isUserExists(response.value.available)
            }
            is Resource.Failure -> {
                loginListener?.signInFail(response.errorBody, response.errorCode)
                if (response.errorCode == 400) {
                    loginListener?.isUserExists(false)
                }
            }
        }
    }

    fun changePassword(changePasswordModel: ChangePasswordModel) = viewModelScope.launch {
        repository.changePassword(changePasswordModel)
    }

    fun forgotPassword(forgotPasswordModel: ForgotPasswordModel) = viewModelScope.launch {
        repository.forgotPassword(forgotPasswordModel)
    }

    fun updateProfile(
        photo: MultipartBody.Part?, email: RequestBody, fullname: RequestBody,
        position: RequestBody, city: RequestBody, country: RequestBody, address: RequestBody,
        organization: RequestBody
    ) = viewModelScope.launch {
        when (val response = repository.updateProfile(
            photo,
            email,
            fullname,
            position,
            city,
            country,
            address,
            organization
        )) {
            is Resource.Success -> {
                userEditListener?.setUserEditData(response.value)
            }
            is Resource.Failure -> {
                loginListener?.signInFail(response.errorBody, response.errorCode)
                if (response.errorCode == 400) {
                    userListener?.dataError(response.errorCode)
                }
            }
        }
        repository.updateProfile(
            photo,
            email,
            fullname,
            position,
            city,
            country,
            address,
            organization
        )
    }

    fun getProfileInfo() = viewModelScope.launch {
        when (val response = repository.getProfileInfo()) {
            is Resource.Success -> {
                userListener?.setUserData(response.value)
            }
            is Resource.Failure -> {
                userListener?.dataError(response.errorCode)
            }
        }
    }

    suspend fun saveAuthToken(tokenAccess: String, tokenRefresh: String) {
        viewModelScope.launch {
            repository.saveAuthToken(tokenAccess, tokenRefresh)
        }
    }

    suspend fun saveLang(lang: String) {
        viewModelScope.launch {
            repository.saveLang(lang)
        }
    }

    suspend fun saveUser(
        email: String, id: Int, username: String, photo: String,
        city: String, country: String, position: String, fullname: String
    ) {
        viewModelScope.launch {
            repository.saveUser(email, id, username, photo, city, country, position, fullname)
        }
    }

    fun setResetPasswordListener(listener: ResetPasswordListener) {
        this.resetPwdListener = listener
    }

    fun setLoginListener(listener: LoginListener) {
        this.loginListener = listener
    }

    fun setRegisterListener(listener: RegisterListener) {
        this.registerListener = listener
    }

    fun setUserListener(listener: UserListener) {
        this.userListener = listener
    }
}