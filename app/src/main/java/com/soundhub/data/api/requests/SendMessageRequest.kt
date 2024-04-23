package com.soundhub.data.api.requests

import java.util.UUID

data class SendMessageRequest(
    val chatId: UUID,
    val userId: UUID,
    val content: String
)
