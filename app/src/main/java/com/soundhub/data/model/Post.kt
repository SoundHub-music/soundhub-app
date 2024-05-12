package com.soundhub.data.model

import java.time.LocalDateTime
import java.util.UUID

data class Post(
    val id: UUID = UUID.randomUUID(),
    var author: User?,
    var publishDate: LocalDateTime = LocalDateTime.now(),
    val content: String = "",
    var images: List<String> = emptyList(),
    var likes: Set<User> = emptySet()
)
