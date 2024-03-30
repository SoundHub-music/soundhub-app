package com.soundhub.data.repository

import com.soundhub.data.api.GenreResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.enums.DiscogsSortType
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Track
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import kotlinx.coroutines.flow.MutableStateFlow

interface MusicRepository {
    suspend fun getAllGenres(countPerPage: Int = 50): HttpResult<GenreResponse>
    suspend fun loadArtistByGenresToState(
        genres: List<String>,
        styles: List<String> = emptyList(),
        page: Int = 1,
        countPerPage: Int = 50,
        artistState: MutableStateFlow<ArtistUiState>
    ): HttpResult<List<Artist>>
    suspend fun getArtistById(artistId: Int): HttpResult<Artist?>

    suspend fun searchArtistByName(artistName: String): HttpResult<Artist?>
    suspend fun getArtistReleases(
        artistId: Int,
        sortType: DiscogsSortType? = null,
        sortOrder: String? = null
    ): HttpResult<List<Track>>
}