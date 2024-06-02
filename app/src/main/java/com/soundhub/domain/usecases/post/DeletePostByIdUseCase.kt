package com.soundhub.domain.usecases.post

import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.repository.PostRepository
import com.soundhub.domain.usecases.UseCaseResult
import java.util.UUID
import javax.inject.Inject

class DeletePostByIdUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(postId: UUID): UseCaseResult<UUID> {

        var errorResponse: ErrorResponse? = null
        var deletedPostId: UUID? = null

        postRepository.deletePost(postId)
            .onSuccess { response -> deletedPostId = response.body }
            .onFailure { error -> errorResponse = error.errorBody }

        return if (errorResponse != null)
            UseCaseResult.Failure(errorResponse)
        else UseCaseResult.Success(deletedPostId)
    }
}