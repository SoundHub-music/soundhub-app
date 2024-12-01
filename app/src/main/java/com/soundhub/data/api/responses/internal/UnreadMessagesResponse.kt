package com.soundhub.data.api.responses.internal

import com.soundhub.data.model.Message

data class UnreadMessagesResponse(
	val messages: List<Message>,
	val count: Int
)
