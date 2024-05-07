package com.soundhub.ui.post_editor

import com.soundhub.data.model.User
import java.time.LocalDateTime
import java.util.UUID

data class PostEditorState(
    var postId: UUID = UUID.randomUUID(),
    var content: String = "",
    var images: List<String> = emptyList(),
    var author: User? = null,
    var publishDate: LocalDateTime = LocalDateTime.now(),
    var likes: Set<User> = emptySet()
)
