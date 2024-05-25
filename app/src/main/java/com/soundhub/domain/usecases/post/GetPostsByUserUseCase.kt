package com.soundhub.domain.usecases.post

import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.data.repository.PostRepository
import com.soundhub.domain.usecases.UseCaseResult
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetPostsByUserUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userCredsStore: UserCredsStore
) {
    suspend operator fun invoke(user: User): UseCaseResult<List<Post>> {
        val userCreds: UserPreferences? = userCredsStore.getCreds()
            .firstOrNull()

        val posts: MutableList<Post> = mutableListOf()
        var errorResponse: ErrorResponse? = null

        postRepository.getPostsByAuthorId(
            authorId = user.id,
            accessToken = userCreds?.accessToken
        ).onSuccess { response ->
            posts.addAll(response.body
                ?.sortedByDescending { p -> p.publishDate }
                .orEmpty()
            )

        }
        .onFailure { error -> errorResponse = error.errorBody }

        return if (errorResponse != null)
            UseCaseResult.Failure(errorResponse)
        else UseCaseResult.Success(posts)
    }
}