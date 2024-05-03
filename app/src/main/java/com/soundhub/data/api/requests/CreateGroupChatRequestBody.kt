package com.soundhub.data.api.requests

import java.util.UUID

data class CreateGroupChatRequestBody(
    val userIds: List<UUID>,
    val groupName: String,
    val chatImage: String?
)
