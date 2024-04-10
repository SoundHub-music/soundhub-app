package com.soundhub.data.repository

import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.api.responses.ApiStateResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Chat

interface ChatRepository {
    suspend fun getAllChatsByCurrentUser(
        accessToken: String?
    ): HttpResult<List<Chat>>

    suspend fun getChatById(
        accessToken: String?,
        chatId: String?
    ): HttpResult<Chat?>

    suspend fun deleteChatById(
        accessToken: String?,
        chatId: String?
    ): HttpResult<ApiStateResponse>

    suspend fun createChat(
        accessToken: String?,
        body: CreateChatRequestBody
    ): HttpResult<Chat>
}