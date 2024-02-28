package com.soundhub.data.model

import java.time.LocalDate
import java.util.UUID

data class Album(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val releaseDate: LocalDate,
    val genre: Genre? = null
)
