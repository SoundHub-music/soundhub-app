package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.PageableMessagesResponse
import com.soundhub.data.api.responses.internal.PagedMessageOrderType
import com.soundhub.data.api.responses.internal.UnreadMessagesResponse
import com.soundhub.data.api.services.MessageService
import com.soundhub.data.sources.MessageSource
import com.soundhub.domain.model.Message
import com.soundhub.domain.repository.BaseRepository
import com.soundhub.domain.repository.MessageRepository
import com.soundhub.utils.constants.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
	private val messageService: MessageService,
	gson: Gson,
	context: Context
) : MessageRepository, BaseRepository(gson, context) {
	override suspend fun getMessageById(
		messageId: UUID
	): HttpResult<Message?> {
		try {
			val response: Response<Message?> = messageService.getMessageById(messageId)
			Log.d("MessageRepository", "getMessageById[1]: $response")
			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("MessageRepository", "getMessageById[2]: ${e.stackTraceToString()}")
			return handleException(e)
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

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("MessageRepository", "getPagedMessages[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getAllUnreadMessages(): HttpResult<UnreadMessagesResponse> {
		try {
			val response = messageService.getAllUnreadMessages()
			Log.d("MessageRepository", "getAllUnreadMessages[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("MessageRepository", "getAllUnreadMessages[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override fun getMessagePage(chatId: UUID, pageSize: Int): Flow<PagingData<Message>> = Pager(
		config = PagingConfig(pageSize = pageSize),
		initialKey = Constants.DEFAULT_MESSAGE_PAGE,
		pagingSourceFactory = {
			MessageSource(
				messageRepository = this,
				chatId = chatId,
				pageSize = pageSize
			)
		}
	).flow
}