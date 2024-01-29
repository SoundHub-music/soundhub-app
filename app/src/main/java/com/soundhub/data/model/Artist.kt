package com.soundhub.data.model

import java.util.UUID

data class Artist(
    val id: UUID = UUID.randomUUID(),
    val name: String? = null
)