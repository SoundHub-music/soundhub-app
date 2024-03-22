package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.UUID

data class Post(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),

    @SerializedName("author")
    val author: User,

    @SerializedName("publishDate")
    val publishDate: LocalDateTime = LocalDateTime.now(),
    val content: String = "",

    @SerializedName("imageContent")
    val imageContent: List<String> = emptyList(),
    val avatar: String? = null,
    var likes: Int = 0
)
