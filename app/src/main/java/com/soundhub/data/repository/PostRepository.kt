package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Post
import java.util.UUID

interface PostRepository {
    suspend fun getPostById(
        accessToken: String?,
        postId: UUID
    ): HttpResult<Post?>
    suspend fun getPostsByAuthorId(
        authorId: UUID,
        accessToken: String?
    ): HttpResult<List<Post>>

    suspend fun addPost(
        post: Post,
        accessToken: String?
    ): HttpResult<Post>

    suspend fun toggleLike(
        accessToken: String?,
        postId: UUID
    ): HttpResult<Post>

    suspend fun deletePost(
        accessToken: String?,
        postId: UUID
    ): HttpResult<UUID>

    suspend fun updatePost(
        accessToken: String?,
        postId: UUID,
        post: Post,
        newImages: List<String> = emptyList(),
        imagesToBeDeleted: List<String> = emptyList()
    ): HttpResult<Post>
}