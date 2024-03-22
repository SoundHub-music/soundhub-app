package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.UUID

data class Album(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    val title: String = "",

    @SerializedName("releaseDate")
    val releaseDate: LocalDate,

    @SerializedName("genre")
    val genre: Genre? = null
)
