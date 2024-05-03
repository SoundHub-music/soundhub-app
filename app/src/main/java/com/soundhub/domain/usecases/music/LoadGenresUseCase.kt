package com.soundhub.domain.usecases.music

import com.soundhub.data.repository.MusicRepository
import com.soundhub.ui.authentication.postregistration.states.GenreUiState
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LoadGenresUseCase @Inject constructor(
    private val musicRepository: MusicRepository,
    private val uiStateDispatcher: UiStateDispatcher
) {
    suspend operator fun invoke(
        countPerPage: Int = 50,
        genreUiState: MutableStateFlow<GenreUiState>
    ) {
        musicRepository.getAllGenres(countPerPage)
            .onSuccess { successResponse ->
                genreUiState.update {
                    it.copy(
                        status = successResponse.status,
                        genres = successResponse.body ?: emptyList()
                    )
                }
            }
            .onFailure { response ->
                genreUiState.update { it.copy(status = response.status) }
                response.errorBody.detail?.let { error ->
                    uiStateDispatcher.sendUiEvent(
                        UiEvent.ShowToast(UiText.DynamicString(error))
                    )
                }
            }
    }
}