package com.soundhub.data.model

import com.soundhub.data.enums.ApiStatus

sealed class ApiResult<T>(val status: ApiStatus, open val data: T?, open val message: String?) {
    data class Success<T>(override val data: T?, override val message: String? = null): ApiResult<T>(
        status = ApiStatus.SUCCESS,
        data = data,
        message = message
    )

    data class Error<T>(override val message: String?, val code: Int? = null): ApiResult<T>(
        status = ApiStatus.ERROR,
        data = null,
        message = message
    )
}