package com.pharos.aalamjobsemployer.ui.auth.utils

import com.pharos.aalamjobsemployer.data.responses.UserResponse

interface UserListener {
    fun setUserData(userResponse: UserResponse)
    fun dataError(code: Int?)
}