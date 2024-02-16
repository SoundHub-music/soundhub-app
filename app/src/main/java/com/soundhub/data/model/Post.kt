package com.soundhub.data.model

import java.util.UUID

data class Post(
    val id: UUID = UUID.randomUUID(),
    val postAuthor: String,
    val publishDate: String,
    val textContent: String,
    val imageContent: List<String> = emptyList(),
    val avatar: String?
)
