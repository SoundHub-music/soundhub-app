package com.soundhub.domain.usecases.music

import com.soundhub.data.repository.MusicRepository
import com.soundhub.data.states.GenreUiState
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.UiText
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
            .onSuccess { response ->
                genreUiState.update {
                    it.copy(
                        status = response.status,
                        genres = response.body.orEmpty()
                    )
                }
            }
            .onFailure { error ->
                genreUiState.update { it.copy(status = error.status) }
                error.errorBody.detail?.let { e ->
                    uiStateDispatcher.sendUiEvent(
                        UiEvent.ShowToast(UiText.DynamicString(e))
                    )
                }
            }
    }
}