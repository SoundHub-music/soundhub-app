package com.soundhub.domain.repository

import androidx.paging.PagingData
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.PageableMessagesResponse
import com.soundhub.data.api.responses.internal.PagedMessageOrderType
import com.soundhub.data.api.responses.internal.UnreadMessagesResponse
import com.soundhub.domain.model.Message
import com.soundhub.utils.constants.Constants
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MessageRepository {
	suspend fun getMessageById(messageId: UUID): HttpResult<Message?>
	suspend fun getPagedMessages(
		chatId: UUID,
		page: Int,
		pageSize: Int,
		sort: String? = null,
		order: PagedMessageOrderType? = null
	): HttpResult<PageableMessagesResponse>

	suspend fun getAllUnreadMessages(): HttpResult<UnreadMessagesResponse>
	fun getMessagePage(
		chatId: UUID,
		pageSize: Int = Constants.DEFAULT_MESSAGE_PAGE_SIZE
	): Flow<PagingData<Message>>
}