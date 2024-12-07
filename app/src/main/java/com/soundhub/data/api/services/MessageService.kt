package com.soundhub.data.api.services

import com.soundhub.data.api.responses.internal.PageableMessagesResponse
import com.soundhub.data.api.responses.internal.PagedMessageOrderType
import com.soundhub.data.api.responses.internal.UnreadMessagesResponse
import com.soundhub.domain.model.Message
import com.soundhub.utils.constants.ApiEndpoints.Message.CHAT_ID_DYNAMIC_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Message.GET_ALL_UNREAD_MESSAGES
import com.soundhub.utils.constants.ApiEndpoints.Message.GET_MESSAGE_BY_ID
import com.soundhub.utils.constants.ApiEndpoints.Message.GET_PAGED_MESSAGES
import com.soundhub.utils.constants.ApiEndpoints.Message.MESSAGE_ID_DYNAMIC_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Message.ORDER_BY_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Message.PAGE_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Message.PAGE_SIZE_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Message.SORT_BY_PARAM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface MessageService {
	@GET(GET_MESSAGE_BY_ID)
	suspend fun getMessageById(
		@Path(MESSAGE_ID_DYNAMIC_PARAM)
		messageId: UUID
	): Response<Message?>

	@GET(GET_PAGED_MESSAGES)
	suspend fun getPagedMessages(
		@Path(CHAT_ID_DYNAMIC_PARAM)
		chatId: UUID,

		@Query(PAGE_PARAM)
		page: Int,

		@Query(PAGE_SIZE_PARAM)
		size: Int,

		@Query(SORT_BY_PARAM)
		sort: String?,

		@Query(ORDER_BY_PARAM)
		order: PagedMessageOrderType?
	): Response<PageableMessagesResponse>

	@GET(GET_ALL_UNREAD_MESSAGES)
	suspend fun getAllUnreadMessages(): Response<UnreadMessagesResponse>
}