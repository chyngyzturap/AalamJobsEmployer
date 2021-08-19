package com.pharos.aalamjobsemployer.ui.auth.utils

import com.pharos.aalamjobsemployer.data.responses.LoginResponse
import okhttp3.ResponseBody

interface LoginListener {
    fun isUserExists(available: Boolean)
    fun signInFail(errorCode: ResponseBody?, code: Int?)

    fun userDataSavedLogin()

    fun loginSuccess(loginResponse: LoginResponse)
    fun loginFail(code: Int?)
}