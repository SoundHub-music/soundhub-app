package com.soundhub.data.model

import java.time.LocalDateTime
import java.util.UUID

data class Post(
    val id: UUID = UUID.randomUUID(),
    val author: User?,
    val publishDate: LocalDateTime = LocalDateTime.now(),
    val content: String = "",
    val images: List<String> = emptyList(),
    var likes: Set<User> = emptySet()
)
