package com.pharos.aalamjobsemployer.ui.auth.register

import com.pharos.aalamjobsemployer.data.responses.LoginResponse
import okhttp3.ResponseBody

interface RegisterListener {
    fun createUserSuccess(username: String)
    fun createUserFailed(code: Int?)
}