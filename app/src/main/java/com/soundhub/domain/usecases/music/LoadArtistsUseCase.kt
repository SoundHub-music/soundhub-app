package com.soundhub.domain.usecases.music

import com.soundhub.data.repository.MusicRepository
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.ui.authentication.postregistration.states.GenreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class LoadArtistsUseCase @Inject constructor(
    private val musicRepository: MusicRepository,
) {
    suspend operator fun invoke(
        genreUiState: MutableStateFlow<GenreUiState>,
        artistUiState: MutableStateFlow<ArtistUiState>,
        page: Int = 1
    ) {
        if (genreUiState.value.chosenGenres.isNotEmpty())
            musicRepository.loadArtistByGenresToState(
                genres = genreUiState.value.chosenGenres.map { it.name ?: "" },
                styles = genreUiState.value.chosenGenres.map { it.name ?: "" },
                artistState = artistUiState,
                page = page
            )
    }
}