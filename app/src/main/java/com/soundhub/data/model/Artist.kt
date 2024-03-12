package com.soundhub.data.model

import java.util.UUID

data class Artist(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String = "",
    val albums: List<Album> = emptyList(),
    val thumbnailUrl: String? = null
)