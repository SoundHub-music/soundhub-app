package com.soundhub.data.api.requests

import java.util.UUID

data class CreateChatRequestBody(
    val recipientId: UUID
)