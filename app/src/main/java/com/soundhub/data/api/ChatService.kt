package com.soundhub.data.api

import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.api.responses.ApiStateResponse
import com.soundhub.data.model.Chat
import com.soundhub.utils.ApiEndpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatService {
    @GET(ApiEndpoints.Chats.GET_CHATS_BY_CURRENT_USER)
    suspend fun getAllChatsByCurrentUser(
        @Header("Authorization") accessToken: String?
    ): Response<List<Chat>>

    @GET(ApiEndpoints.Chats.GET_CHAT_BY_ID)
    suspend fun getChatById(
        @Header("Authorization") accessToken: String?,
        @Path(ApiEndpoints.Chats.CHAT_ID_DYNAMIC_PARAM)
        chatId: String?
    ): Response<Chat?>

    @DELETE(ApiEndpoints.Chats.DELETE_CHAT)
    suspend fun deleteChatById(
        @Header("Authorization") accessToken: String?,
        @Path(ApiEndpoints.Chats.CHAT_ID_DYNAMIC_PARAM)
        chatId: String?
    ): Response<ApiStateResponse>

    @POST(ApiEndpoints.Chats.CREATE_CHAT)
    suspend fun createChat(
        @Header("Authorization") accessToken: String?,
        @Body body: CreateChatRequestBody
    ): Response<Chat>
}