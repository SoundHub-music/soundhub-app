package com.soundhub.data.api.services

import com.soundhub.data.model.Message
import com.soundhub.utils.constants.ApiEndpoints.Message.CHAT_ID_DYNAMIC_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Message.GET_CHAT_MESSAGES
import com.soundhub.utils.constants.ApiEndpoints.Message.GET_MESSAGE_BY_ID
import com.soundhub.utils.constants.ApiEndpoints.Message.MESSAGE_ID_DYNAMIC_PARAM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface MessageService {
    @GET(GET_CHAT_MESSAGES)
    suspend fun getAllMessagesByChatId(
        @Path(CHAT_ID_DYNAMIC_PARAM)
        chatId: UUID
    ): Response<List<Message>>

    @GET(GET_MESSAGE_BY_ID)
    suspend fun getMessageById(
        @Path(MESSAGE_ID_DYNAMIC_PARAM)
        messageId: UUID
    ): Response<Message?>

}