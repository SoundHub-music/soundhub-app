package com.soundhub.domain.usecases.post

import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.model.Post
import com.soundhub.data.repository.PostRepository
import com.soundhub.domain.usecases.UseCaseResult
import java.util.UUID
import javax.inject.Inject

class TogglePostLikeAndUpdateListUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(postId: UUID, posts: List<Post>): UseCaseResult<List<Post>> {
        var updatedPostList: List<Post> = posts
        var errorResponse: ErrorResponse? = null

        postRepository.toggleLike(postId)
            .onSuccess { response -> updatedPostList = updatePostInList(response.body, posts) }
            .onFailure { error -> errorResponse = error.errorBody }

        return if (errorResponse != null)
            UseCaseResult.Failure(errorResponse)
        else UseCaseResult.Success(updatedPostList)
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