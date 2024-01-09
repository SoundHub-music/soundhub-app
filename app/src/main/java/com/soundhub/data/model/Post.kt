package com.soundhub.data.model

data class Post(
    val id: Int,
    val postAuthor: String,
    val publishDate: String,
    val textContent: String,
    val imageContent: List<String>?,
    val avatar: String?
)
