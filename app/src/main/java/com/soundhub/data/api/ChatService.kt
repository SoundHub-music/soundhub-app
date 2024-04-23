package com.soundhub.data.api

import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.api.responses.ApiStateResponse
import com.soundhub.data.model.Chat
import com.soundhub.utils.ApiEndpoints
import com.soundhub.utils.HttpUtils
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface ChatService {
    @GET(ApiEndpoints.Chats.GET_CHATS_BY_CURRENT_USER)
    suspend fun getAllChatsByCurrentUser(
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?
    ): Response<List<Chat>>

    @GET(ApiEndpoints.Chats.GET_CHAT_BY_ID)
    suspend fun getChatById(
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?,
        @Path(ApiEndpoints.Chats.CHAT_ID_DYNAMIC_PARAM)
        chatId: UUID
    ): Response<Chat?>

    @DELETE(ApiEndpoints.Chats.DELETE_CHAT)
    suspend fun deleteChatById(
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?,
        @Path(ApiEndpoints.Chats.CHAT_ID_DYNAMIC_PARAM)
        chatId: UUID
    ): Response<ApiStateResponse>

    @POST(ApiEndpoints.Chats.CREATE_CHAT)
    suspend fun createChat(
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?,
        @Body body: CreateChatRequestBody
    ): Response<Chat>
}