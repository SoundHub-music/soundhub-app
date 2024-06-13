package com.soundhub.domain.usecases.post

import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.model.Post
import com.soundhub.data.repository.PostRepository
import java.util.UUID
import javax.inject.Inject

class TogglePostLikeAndUpdateListUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(postId: UUID, posts: List<Post>): Result<List<Post>> = kotlin.runCatching {
        var updatedPostList: List<Post> = posts
        var throwable: Throwable? = null
        var errorResponse: ErrorResponse? = null

        postRepository.toggleLike(postId)
            .onSuccess { response -> updatedPostList = updatePostInList(response.body, posts) }
            .onFailure { error ->
                throwable = error.throwable
                errorResponse = error.errorBody
            }

        throwable?.let { throw Exception(errorResponse?.detail, throwable) }
        return@runCatching updatedPostList
    }

    private fun updatePostInList(updatedPost: Post?, posts: List<Post>): List<Post> {
        val updatedPostList: List<Post> = posts.map { post ->
            updatedPost?.let {
                if (post.id == it.id) {
                    post.likes += updatedPost.likes
                    post
                }
                else post
            }
            post
        }

        return updatedPostList
    }
}