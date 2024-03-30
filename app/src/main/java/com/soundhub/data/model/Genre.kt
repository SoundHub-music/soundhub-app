package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Genre(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    val name: String? = null,
    val pictureUrl: String? = null
)
