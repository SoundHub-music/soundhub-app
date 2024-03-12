package com.soundhub.ui.states

import com.soundhub.data.model.Post

data class PostlineUiState(
    var isLoading: Boolean = false,
    val posts: List<Post> = emptyList()
)
