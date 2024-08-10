package com.soundhub.data.api.responses

import com.soundhub.data.enums.ApiStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class HttpResult<T>(val status: ApiStatus) {
	data class Success<T>(
		val body: T?,
		val message: String? = null
	) : HttpResult<T>(status = ApiStatus.SUCCESS)

	data class Error<T>(
		val errorBody: ErrorResponse,
		val throwable: Exception? = null
	) : HttpResult<T>(status = ApiStatus.ERROR)

	suspend fun onSuccess(callback: suspend (Success<T>) -> Unit): HttpResult<T> {
		if (this is Success) callback(this)
		return this
	}

	suspend fun onSuccessWithContext(
		context: CoroutineDispatcher = Dispatchers.Main,
		callback: suspend (Success<T>) -> Unit
	): HttpResult<T> = withContext(context) { onSuccess(callback) }

	fun onSuccessReturn(): T? {
		if (this is Success)
			return this.body
		return null
	}

	suspend fun onFailure(callback: suspend (Error<T>) -> Unit): HttpResult<T> {
		if (this is Error) callback(this)
		return this
	}

	suspend fun onFailureWithContext(
		context: CoroutineDispatcher = Dispatchers.Main,
		callback: suspend (Error<T>) -> Unit
	): HttpResult<T> = withContext(context) { onFailure(callback) }

	suspend fun finally(
		callback: suspend (HttpResult<T>) -> Unit
	): HttpResult<T> {
		if (this is Success || this is Error)
			callback(this)
		return this
	}

	suspend fun finallyWithContext(
		context: CoroutineDispatcher = Dispatchers.Main,
		callback: suspend (HttpResult<T>) -> Unit
	): HttpResult<T> = withContext(context) { onSuccess(callback) }

	fun getOrNull(): T? = if (this is Success) body else null
}
