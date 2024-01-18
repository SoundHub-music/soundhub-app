package com.soundhub.data.model

import java.util.UUID

data class Genre(
    val id: UUID = UUID.randomUUID(),
    val name: String? = null
)
