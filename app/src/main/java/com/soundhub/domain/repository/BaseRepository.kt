package com.soundhub.domain.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.R
import com.soundhub.data.api.responses.internal.ErrorResponse
import com.soundhub.data.api.responses.internal.HttpResult
import retrofit2.Response

abstract class BaseRepository(
	private val gson: Gson,
	private val context: Context
) {
	protected suspend fun <In, Out> handleResponse(
		response: Response<In>,
		prepareDataCallback: suspend () -> HttpResult<Out>
	): HttpResult<Out> {
		return if (response.isSuccessful) {
			prepareDataCallback()
		} else getError<In, Out>(response)
	}

	protected suspend fun <T> handleResponse(
		response: Response<T>,
		beforeReturningActions: (suspend () -> Unit)? = null,
		prepareDataCallback: (suspend () -> HttpResult<T>)? = null,
	): HttpResult<T> {
		return if (response.isSuccessful) {
			beforeReturningActions?.invoke()
			prepareDataCallback
				?.let { prepareDataCallback() }
				?: HttpResult.Success(response.body())
		} else getError<T, T>(response)
	}

	private fun <In, Out> getError(response: Response<In>): HttpResult<Out> {
		val errorBody = gson.fromJson(
			response.errorBody()?.charStream(),
			ErrorResponse::class.java
		)?.apply {
			if (message == null)
				message = detail

			if (detail == null)
				detail = message

			status = response.code()
		}
			?: ErrorResponse(
				status = response.code(),
				detail = context.getString(R.string.toast_common_error),
				message = context.getString(R.string.toast_common_error),
			)
		Log.e("BaseRepository", "Error: $errorBody")
		return HttpResult.Error(errorBody = errorBody)
	}

	protected fun <T> handleException(e: Exception): HttpResult<T> {
		Log.e("BaseRepository", "Exception: ${e.stackTraceToString()}")
		return HttpResult.Error(
			errorBody = ErrorResponse(detail = e.localizedMessage),
			throwable = e
		)
	}
}
