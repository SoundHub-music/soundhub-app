package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Post
import okhttp3.RequestBody
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
    ): HttpResult<RequestBody>

    suspend fun updatePost(
        accessToken: String?,
        postId: UUID,
        post: Post,
        deletedImages: List<String> = emptyList()
    ): HttpResult<Post>
}