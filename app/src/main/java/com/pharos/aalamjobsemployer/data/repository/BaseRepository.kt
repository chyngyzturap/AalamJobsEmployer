package com.pharos.aalamjobsemployer.data.repository

import android.util.Log
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        Log.e("ololo", "safeApiCall: error ${throwable.message}")
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }

    suspend fun logout(api: AuthApi) = safeApiCall {
        api.logout()
    }
}