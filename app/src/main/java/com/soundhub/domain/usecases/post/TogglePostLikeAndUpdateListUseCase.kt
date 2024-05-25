package com.soundhub.domain.usecases.post

import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Post
import com.soundhub.data.repository.PostRepository
import com.soundhub.domain.usecases.UseCaseResult
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class TogglePostLikeAndUpdateListUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userCredsStore: UserCredsStore
) {
    suspend operator fun invoke(postId: UUID, posts: List<Post>): UseCaseResult<List<Post>> {
        val creds: UserPreferences? = userCredsStore.getCreds()
            .firstOrNull()

        var updatedPostList: List<Post> = posts
        var errorResponse: ErrorResponse? = null

        postRepository.toggleLike(
            accessToken = creds?.accessToken,
            postId = postId
        )
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