package com.soundhub.data.model

import java.util.UUID

data class Track(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val duration: Int
)
