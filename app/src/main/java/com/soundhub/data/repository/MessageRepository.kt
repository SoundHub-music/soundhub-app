package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.PageableMessagesResponse
import com.soundhub.data.api.responses.PagedMessageOrderType
import com.soundhub.data.api.responses.UnreadMessagesResponse
import com.soundhub.data.model.Message
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
}