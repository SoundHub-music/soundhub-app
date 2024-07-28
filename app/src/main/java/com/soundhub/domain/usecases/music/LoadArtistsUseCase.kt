package com.soundhub.domain.usecases.music

import com.soundhub.data.api.responses.discogs.DiscogsEntityResponse
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Artist
import com.soundhub.data.repository.MusicRepository
import com.soundhub.data.states.ArtistUiState
import com.soundhub.data.states.GenreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LoadArtistsUseCase @Inject constructor(
    private val musicRepository: MusicRepository,
) {
    suspend operator fun invoke(
        genreUiState: GenreUiState,
        artistUiState: MutableStateFlow<ArtistUiState>,
        paginationUrl: String? = null
    ): Result<List<Artist>> = runCatching {
        var result: List<Artist> = emptyList()
        if (genreUiState.chosenGenres.isNotEmpty()) {
            paginationUrl?.let { url ->
                artistUiState.update { it.copy(status = ApiStatus.LOADING) }
                musicRepository.getDiscogsDataFromUrl(url)
                    .onSuccess { response ->
                        loadDataToArtistState(
                            data = response.body?.results.orEmpty(),
                            artistUiState = artistUiState
                        ).also { artistUiState.update { it.copy(status = ApiStatus.SUCCESS) } }
                    }
                    .onFailure { error ->
                        artistUiState.update { it.copy(status = ApiStatus.ERROR) }

                        error.throwable?.let { throw error.throwable }
                            ?: throw Exception(error.errorBody.detail)
                    }

            } ?: musicRepository.getArtistsByGenres(
                genres = genreUiState.chosenGenres.map { it.name ?: "" },
                styles = genreUiState.chosenGenres.map { it.name ?: "" },
            ).onSuccess { response ->
                result = loadDataToArtistState(
                    data = response.body?.results.orEmpty(),
                    artistUiState = artistUiState
                )

                artistUiState.update { it.copy(pagination = response.body?.pagination) }

            }.onFailure { error ->
                error.throwable?.let { throw Exception(error.throwable) }
                throw Exception(error.errorBody.title)
            }
        }

        return@runCatching result
    }

    private suspend fun loadDataToArtistState(
        data: List<DiscogsEntityResponse>,
        artistUiState: MutableStateFlow<ArtistUiState>
    ): List<Artist> {
        val duplicateEntityRegex = Regex("\\p{L}+(?:\\s+\\p{L}+)*\\s*\\(\\d+\\)")
        val result: MutableList<Artist> = mutableListOf()

        data.forEach { entity ->
            val artistName: String = entity.title.split("-")[0].trim()

            musicRepository.searchArtistInReleaseResponse(artistName)
                .onSuccess { artist ->
                    artist.body?.let { artistBody ->
                        artistBody.genre = entity.genre.orEmpty()
                        artistBody.style = entity.style.orEmpty()
                        val state = artistUiState.value.artists

                        val artistNotInState: Boolean = artistBody.name?.lowercase() !in state.map { it.name?.lowercase() }
                        val isDuplicate: Boolean = duplicateEntityRegex.matches(artistBody.name ?: "")

                        if (artistNotInState && !isDuplicate) {
                            artistUiState.update {
                                it.copy(artists = (it.artists + artistBody))
                            }
                            result.add(artistBody)
                        }

                    }
                }
        }
        return result.distinctBy { it.name }
    }
}