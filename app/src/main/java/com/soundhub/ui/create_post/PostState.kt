package com.soundhub.ui.create_post

import com.soundhub.data.model.User

data class PostState(
    val content: String = "",
    val images: List<String> = emptyList(),
    val author: User? = null
)
