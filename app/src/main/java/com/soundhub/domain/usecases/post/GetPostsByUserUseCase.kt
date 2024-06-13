package com.soundhub.domain.usecases.post

import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import javax.inject.Inject

class GetPostsByUserUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(user: User): Result<List<Post>> = runCatching {
        val posts: MutableList<Post> = mutableListOf()
        var errorResponse: ErrorResponse? = null
        var throwable: Throwable? = null

        postRepository.getPostsByAuthorId(
            authorId = user.id
        ).onSuccess { response ->
            posts.addAll(response.body
                ?.sortedByDescending { p -> p.publishDate }
                .orEmpty()
            )

        }
        .onFailure { error ->
            errorResponse = error.errorBody
            throwable = error.throwable
        }

        if (errorResponse != null || throwable != null) {
            throw Exception(errorResponse?.detail, throwable)
        }

        return@runCatching posts
    }
}