package com.soundhub.domain.repository

import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.api.responses.internal.ApiStateResponse
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.model.Chat
import java.util.UUID

interface ChatRepository {
	suspend fun getAllChatsByUserId(userId: UUID): HttpResult<List<Chat>>
	suspend fun getChatById(chatId: UUID): HttpResult<Chat?>
	suspend fun deleteChatById(chatId: UUID): HttpResult<ApiStateResponse>
	suspend fun createChat(body: CreateChatRequestBody): HttpResult<Chat>
}