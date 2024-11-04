package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.api.responses.ApiStateResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.services.ChatService
import com.soundhub.data.model.Chat
import com.soundhub.data.repository.BaseRepository
import com.soundhub.data.repository.ChatRepository
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
	private val chatService: ChatService,
	gson: Gson,
	context: Context
) : ChatRepository, BaseRepository(gson, context) {
	override suspend fun getAllChatsByUserId(userId: UUID): HttpResult<List<Chat>> {
		try {
			val response: Response<List<Chat>> = chatService
				.getAllChatsByUserId(userId)
			Log.d("ChatRepository", "getAllChatsByUserId[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("ChatRepository", "getAllChatsByUserId[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getChatById(chatId: UUID): HttpResult<Chat?> {
		try {
			val response: Response<Chat?> = chatService.getChatById(chatId = chatId)
			Log.d("ChatRepository", "getChatById[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("ChatRepository", "getChatById[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun deleteChatById(chatId: UUID): HttpResult<ApiStateResponse> {
		try {
			val response: Response<ApiStateResponse> = chatService
				.deleteChatById(chatId = chatId)
			Log.d("ChatRepository", "deleteChatById[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("ChatRepository", "deleteChatById[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun createChat(
		body: CreateChatRequestBody
	): HttpResult<Chat> {
		try {
			val response: Response<Chat> = chatService.createChat(body)
			Log.d("ChatRepository", "createChat[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("ChatRepository", "createChat[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}
}