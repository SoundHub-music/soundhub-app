package com.soundhub.domain.usecases

import com.soundhub.data.api.responses.ErrorResponse

sealed class UseCaseResult<T>{
    data class Success<T>(val data: T? = null): UseCaseResult<T>()
    data class Failure<T>(val error: ErrorResponse?): UseCaseResult<T>()
}