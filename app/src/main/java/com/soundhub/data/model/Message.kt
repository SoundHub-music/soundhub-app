package com.soundhub.data.model

import java.time.LocalDateTime

data class Message(
    val time: LocalDateTime,
    val text: String
)
