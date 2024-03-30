package com.soundhub.data.repository.implementations

import com.soundhub.data.api.ChatService
import com.soundhub.data.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService
): ChatRepository {

}