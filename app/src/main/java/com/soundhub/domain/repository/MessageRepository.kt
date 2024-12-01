package com.soundhub.domain.repository

import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.PageableMessagesResponse
import com.soundhub.data.api.responses.internal.PagedMessageOrderType
import com.soundhub.data.api.responses.internal.UnreadMessagesResponse
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