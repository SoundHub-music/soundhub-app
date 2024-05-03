package com.soundhub.domain.usecases.chat

import android.util.Log
import com.soundhub.data.api.requests.CreateChatRequestBody
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
import javax.inject.Inject

class GetOrCreateChatByUserUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        interlocutor: User?,
        accessToken: String?
    ): Chat? {
        if (interlocutor == null)
            return null

        val allChats: List<Chat> = chatRepository
            .getAllChatsByCurrentUser(accessToken)
            .onFailure { Log.e("GetOrCreateChatUseCase", "get all chats error: $it") }
            .getOrNull()
            ?: emptyList()

        val isChatExists: Boolean = allChats.any { chat -> interlocutor in chat.participants }

        return if (!isChatExists)
            chatRepository.createChat(
                accessToken = accessToken,
                body = CreateChatRequestBody(interlocutor.id)
            ).onFailure { Log.e("GetOrCreateChatUseCase", "create chat error: $it") }
            .getOrNull()
        else allChats.first { interlocutor in it.participants }
    }
}