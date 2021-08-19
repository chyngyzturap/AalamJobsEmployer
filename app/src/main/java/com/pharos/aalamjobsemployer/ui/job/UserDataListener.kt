package com.pharos.aalamjobsemployer.ui.job

import com.pharos.aalamjobsemployer.data.responses.UserResponse

interface UserDataListener {
    fun setUserData(userResponse: UserResponse)
    fun dataError(code: Int?)
}