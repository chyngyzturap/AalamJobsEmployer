package com.pharos.aalamjobsemployer.ui.auth.utils

import com.pharos.aalamjobsemployer.data.responses.UserResponse

interface UserEditListener {
    fun setUserEditData(userResponse: UserResponse)
    fun dataEditError(code: Int?)
}