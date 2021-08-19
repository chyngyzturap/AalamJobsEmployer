package com.pharos.aalamjobsemployer.ui.base

import androidx.lifecycle.ViewModel
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.repository.BaseRepository


abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {
    suspend fun logout(api: AuthApi) = repository.logout(api)
}