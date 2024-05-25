package com.soundhub.domain.usecases.post

import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.repository.PostRepository
import com.soundhub.domain.usecases.UseCaseResult
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class DeletePostByIdUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userCredsStore: UserCredsStore
) {
    suspend operator fun invoke(postId: UUID): UseCaseResult<UUID> {
        val creds: UserPreferences? = userCredsStore.getCreds()
            .firstOrNull()

        var errorResponse: ErrorResponse? = null
        var deletedPostId: UUID? = null

        postRepository.deletePost(
            accessToken = creds?.accessToken,
            postId = postId
        )
            .onSuccess { response -> deletedPostId = response.body }
            .onFailure { error -> errorResponse = error.errorBody }

        return if (errorResponse != null)
            UseCaseResult.Failure(errorResponse)
        else UseCaseResult.Success(deletedPostId)
    }
}