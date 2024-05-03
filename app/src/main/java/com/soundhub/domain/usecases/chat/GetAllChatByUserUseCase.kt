package com.soundhub.domain.usecases.chat

import android.util.Log
import com.soundhub.data.model.Chat
import com.soundhub.data.repository.ChatRepository
import javax.inject.Inject

class GetAllChatsByUserUseCase @Inject constructor(
   private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(accessToken: String?): List<Chat> {
        var chats: List<Chat> = emptyList()
        chatRepository.getAllChatsByCurrentUser(accessToken)
            .onSuccess { response -> chats = response.body ?: emptyList() }
            .onFailure { Log.e("GetAllChatByUserUseCase", "error: $it") }

        return chats
    }
}