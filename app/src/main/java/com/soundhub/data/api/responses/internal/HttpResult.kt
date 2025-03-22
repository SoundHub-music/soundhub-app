package com.soundhub.data.api.responses.internal

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

	private var isSuccess: Boolean = false
		get() = this is Success

	private var isFailure: Boolean = false
		get() = this is Error

	suspend fun onSuccess(callback: suspend (Success<T>) -> Unit): HttpResult<T> {
		if (this is Success) callback(this)
		return this
	}

	suspend fun onSuccessGet(callback: suspend (Success<T>) -> T): T? {
		if (this is Success) return callback(this)
		return null
	}

	suspend fun onSuccessWithContext(
		context: CoroutineDispatcher = Dispatchers.Main,
		callback: suspend (Success<T>) -> Unit
	): HttpResult<T> = withContext(context) { onSuccess(callback) }

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

	fun <X : Exception> getOrThrow(callback: () -> X): HttpResult<T> {
		if (this is Success)
			return this

		throw callback()
	}

	fun getOrThrow(): T? {
		if (this is Success)
			return this.body

		var throwable: Exception? = null
		var errorDetail: String? = null

		if (this is Error) {
			errorDetail = this.errorBody.detail
			throwable = this.throwable
		}

		val message: String? = errorDetail ?: throwable?.message
		throw throwable ?: IllegalStateException(message)
	}
}
