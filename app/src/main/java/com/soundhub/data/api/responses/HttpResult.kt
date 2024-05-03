package com.soundhub.data.api.responses

import com.soundhub.data.enums.ApiStatus

sealed class HttpResult<T>(val status: ApiStatus) {
    data class Success<T>(
        val body: T?,
        val message: String? = null
    ): HttpResult<T>(status = ApiStatus.SUCCESS)

    data class Error<T>(
        val errorBody: ErrorResponse,
        val throwable: Exception? = null
    ): HttpResult<T>(status = ApiStatus.ERROR)

    suspend fun onSuccess(callback: suspend (Success<T>) -> Unit): HttpResult<T> {
        if (this is Success) callback(this)
        return this
    }

    suspend fun onFailure(callback: suspend (Error<T>) -> Unit): HttpResult<T> {
        if (this is Error) callback(this)
        return this
    }

    suspend fun finally(callback: suspend (HttpResult<T>) -> Unit): HttpResult<T> {
        if (this is Success || this is Error)
            callback(this)
        return this
    }

    fun getOrNull(): T? = if (this is Success) body else null
}
