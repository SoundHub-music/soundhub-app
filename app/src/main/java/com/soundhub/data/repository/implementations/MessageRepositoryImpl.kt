package com.soundhub.data.repository.implementations

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.services.MessageService
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Message
import com.soundhub.data.repository.MessageRepository
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import com.soundhub.utils.constants.Constants
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageService: MessageService,
    private val loadAllUserDataUseCase: LoadAllUserDataUseCase
): MessageRepository {
    override suspend fun getAllMessagesByChatId(
        chatId: UUID
    ): HttpResult<List<Message>> {
        try {
            val response: Response<List<Message>> = messageService.getAllMessagesByChatId(chatId)

            Log.d("MessageRepository", "getAllMessagesByChatId[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())
                Log.e("MessageRepository", "getAllMessagesByChatId[2]: $errorResponse")
                return HttpResult.Error(errorBody = errorResponse)
            }

            val messages: List<Message> = response.body().orEmpty()
            messages.forEach { msg -> msg.sender?.let { loadAllUserDataUseCase(it) } }

            return HttpResult.Success(body = messages)
        }
        catch (e: Exception) {
            Log.e("MessageRepository", "getAllMessagesByChatId[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                throwable = e,
                errorBody = ErrorResponse(detail = e.localizedMessage)
            )
        }
    }

    override suspend fun getMessageById(
        messageId: UUID
    ): HttpResult<Message?> {
        try {
            val response: Response<Message?> = messageService.getMessageById(messageId)

            Log.d("MessageRepository", "getMessageById[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())
                Log.e("MessageRepository", "getMessageById[2]: $errorResponse")
                return HttpResult.Error(errorBody = errorResponse)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("MessageRepository", "getMessageById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                throwable = e,
                errorBody = ErrorResponse(detail = e.localizedMessage)
            )
        }
    }
}