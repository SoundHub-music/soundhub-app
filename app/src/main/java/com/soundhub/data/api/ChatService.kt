package com.soundhub.data.api

import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.api.requests.CreateGroupChatRequestBody
import com.soundhub.data.api.responses.ApiStateResponse
import com.soundhub.data.model.Chat
import com.soundhub.utils.ApiEndpoints.Chats.ADD_USER_TO_GROUP
import com.soundhub.utils.ApiEndpoints.Chats.CHAT_ID_DYNAMIC_PARAM
import com.soundhub.utils.ApiEndpoints.Chats.CHAT_NAME_DYNAMIC_PARAM
import com.soundhub.utils.ApiEndpoints.Chats.CREATE_CHAT
import com.soundhub.utils.ApiEndpoints.Chats.CREATE_GROUP_CHAT
import com.soundhub.utils.ApiEndpoints.Chats.DELETE_CHAT
import com.soundhub.utils.ApiEndpoints.Chats.DELETE_USER_FROM_GROUP
import com.soundhub.utils.ApiEndpoints.Chats.GET_CHATS_BY_CURRENT_USER
import com.soundhub.utils.ApiEndpoints.Chats.GET_CHAT_BY_ID
import com.soundhub.utils.ApiEndpoints.Chats.RENAME_GROUP_CHAT
import com.soundhub.utils.ApiEndpoints.Chats.USER_ID_DYNAMIC_PARAM
import com.soundhub.utils.HttpUtils.Companion.AUTHORIZATION_HEADER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ChatService {
    @GET(GET_CHATS_BY_CURRENT_USER)
    suspend fun getAllChatsByUserId(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(USER_ID_DYNAMIC_PARAM)
        userId: UUID
    ): Response<List<Chat>>

    @GET(GET_CHAT_BY_ID)
    suspend fun getChatById(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(CHAT_ID_DYNAMIC_PARAM)
        chatId: UUID
    ): Response<Chat?>

    @DELETE(DELETE_CHAT)
    suspend fun deleteChatById(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(CHAT_ID_DYNAMIC_PARAM)
        chatId: UUID
    ): Response<ApiStateResponse>

    @POST(CREATE_CHAT)
    suspend fun createChat(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Body
        body: CreateChatRequestBody
    ): Response<Chat>

    @POST(CREATE_GROUP_CHAT)
    suspend fun createGroupChat(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Body
        body: CreateGroupChatRequestBody
    ): Response<Chat>

    @PUT(ADD_USER_TO_GROUP)
    suspend fun addUserToGroup(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(CHAT_ID_DYNAMIC_PARAM)
        chatId: UUID,
        @Path(USER_ID_DYNAMIC_PARAM)
        userId: UUID
    ): Response<Chat>

    @PUT(DELETE_USER_FROM_GROUP)
    suspend fun deleteUserFromGroupChat(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(CHAT_ID_DYNAMIC_PARAM)
        chatId: UUID,
        @Path(USER_ID_DYNAMIC_PARAM)
        userId: UUID
    ): Response<Chat>

    @PUT(RENAME_GROUP_CHAT)
    suspend fun renameGroupChat(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(CHAT_ID_DYNAMIC_PARAM)
        chatId: UUID,
        @Path(CHAT_NAME_DYNAMIC_PARAM)
        newChatName: String
    ): Response<Chat>
}