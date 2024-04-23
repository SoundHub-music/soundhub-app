package com.soundhub.ui.post_editor

import com.soundhub.data.model.User

data class PostEditorState(
    val content: String = "",
    val images: List<String> = emptyList(),
    val author: User? = null
)
