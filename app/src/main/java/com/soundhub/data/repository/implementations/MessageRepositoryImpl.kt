package com.soundhub.data.repository.implementations

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.PageableMessagesResponse
import com.soundhub.data.api.responses.PagedMessageOrderType
import com.soundhub.data.api.responses.UnreadMessagesResponse
import com.soundhub.data.api.services.MessageService
import com.soundhub.data.model.Message
import com.soundhub.data.repository.MessageRepository
import com.soundhub.utils.constants.Constants
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
	private val messageService: MessageService
) : MessageRepository {
	override suspend fun getMessageById(
		messageId: UUID
	): HttpResult<Message?> {
		try {
			val response: Response<Message?> = messageService.getMessageById(messageId)

			Log.d("MessageRepository", "getMessageById[1]: $response")

			if (!response.isSuccessful) {
				val errorResponse: ErrorResponse = Gson()
					.fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(status = response.code())
				Log.e("MessageRepository", "getMessageById[2]: $errorResponse")
				return HttpResult.Error(errorBody = errorResponse)
			}

			return HttpResult.Success(body = response.body())
		} catch (e: Exception) {
			Log.e("MessageRepository", "getMessageById[3]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				throwable = e,
				errorBody = ErrorResponse(detail = e.localizedMessage)
			)
		}
	}

	override suspend fun getPagedMessages(
		chatId: UUID,
		page: Int,
		pageSize: Int,
		sort: String?,
		order: PagedMessageOrderType?
	): HttpResult<PageableMessagesResponse> {
		try {
			val response = messageService.getPagedMessages(
				chatId = chatId,
				page = page,
				size = pageSize,
				sort = sort,
				order = order
			)

			Log.d("MessageRepository", "getPagedMessages[1]: $response")

			if (!response.isSuccessful) {
				val errorResponse: ErrorResponse = Gson()
					.fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(status = response.code())
				Log.e("MessageRepository", "getPagedMessages[2]: $errorResponse")
				return HttpResult.Error(errorBody = errorResponse)
			}

			return HttpResult.Success(body = response.body())
		}
		catch (e: Exception) {
			Log.e("MessageRepository", "getPagedMessages[3]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				throwable = e,
				errorBody = ErrorResponse(detail = e.localizedMessage)
			)
		}
	}

	override suspend fun getAllUnreadMessages(): HttpResult<UnreadMessagesResponse> {
		try {
			val response = messageService.getAllUnreadMessages()
			Log.d("MessageRepository", "getAllUnreadMessages[1]: $response")

			if (!response.isSuccessful) {
				val errorResponse: ErrorResponse = Gson()
					.fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(status = response.code())
				Log.e("MessageRepository", "getAllUnreadMessages[2]: $errorResponse")
				return HttpResult.Error(errorBody = errorResponse)
			}

			return HttpResult.Success(body = response.body())
		}
		catch (e: Exception) {
			Log.e("MessageRepository", "getAllUnreadMessages[3]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				throwable = e,
				errorBody = ErrorResponse(detail = e.localizedMessage)
			)
		}
	}
}