package com.soundhub.data.model

import java.util.UUID

data class Playlist(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val author: User? = null,
    val tracks: List<Track> = emptyList()
)
