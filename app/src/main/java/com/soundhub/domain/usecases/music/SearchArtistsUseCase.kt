package com.soundhub.domain.usecases.music

import com.soundhub.data.model.Artist
import com.soundhub.data.repository.MusicRepository
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SearchArtistsUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(searchBarValue: String, artistStateFlow: MutableStateFlow<ArtistUiState>) {
        delay(500)
        if (searchBarValue.isNotEmpty()) {
            musicRepository.searchArtists(searchBarValue)
                .onSuccess { response ->
                    val artists: List<Artist> = response.body ?: emptyList()
                    artistStateFlow.update { it.copy(artists = artists) }
                }
        }
    }
}