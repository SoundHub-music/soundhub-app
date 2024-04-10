package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.R
import com.soundhub.data.api.ChatService
import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.api.responses.ApiStateResponse
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Chat
import com.soundhub.data.repository.ChatRepository
import com.soundhub.utils.Constants
import retrofit2.Response
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService,
    private val context: Context
): ChatRepository {
    override suspend fun getAllChatsByCurrentUser(accessToken: String?): HttpResult<List<Chat>> {
        try {
            val response: Response<List<Chat>> = chatService
                .getAllChatsByCurrentUser("Bearer $accessToken")
            Log.d("ChatRepository", "getAllChatsByCurrentUser[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_logout_error)
                    )

                Log.d("ChatRepository", "getAllChatsByCurrentUser[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "getAllChatsByCurrentUser[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getChatById(accessToken: String?, chatId: String?): HttpResult<Chat?> {
        try {
            val response: Response<Chat?> = chatService
                .getChatById(
                    accessToken = "Bearer $accessToken",
                    chatId = chatId
            )

            Log.d("ChatRepository", "getChatById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_logout_error)
                    )

                Log.d("ChatRepository", "getChatById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "getChatById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun deleteChatById(
        accessToken: String?,
        chatId: String?
    ): HttpResult<ApiStateResponse> {
        try {
            val response: Response<ApiStateResponse> = chatService
                .deleteChatById(
                    accessToken = "Bearer $accessToken",
                    chatId = chatId
            )
            Log.d("ChatRepository", "deleteChatById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_logout_error)
                    )

                Log.d("ChatRepository", "deleteChatById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "deleteChatById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun createChat(
        accessToken: String?,
        body: CreateChatRequestBody
    ): HttpResult<Chat> {
        try {
            val response: Response<Chat> = chatService
                .createChat(
                    accessToken = "Bearer $accessToken",
                    body = body
            )
            Log.d("ChatRepository", "createChat[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_logout_error)
                    )

                Log.d("ChatRepository", "createChat[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "createChat[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

}