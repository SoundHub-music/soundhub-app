package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Message
import java.util.UUID

interface MessageRepository {
	suspend fun getAllMessagesByChatId(chatId: UUID): HttpResult<List<Message>>
	suspend fun getMessageById(messageId: UUID): HttpResult<Message?>
}