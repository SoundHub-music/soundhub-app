package com.soundhub.domain.repository

import androidx.paging.PagingData
import com.soundhub.data.api.responses.discogs.DiscogsResponse
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.enums.DiscogsSearchType
import com.soundhub.data.enums.DiscogsSortType
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Genre
import com.soundhub.domain.model.Track
import com.soundhub.domain.states.GenreUiState
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
	suspend fun getAllGenres(countPerPage: Int = 50): HttpResult<List<Genre>>
	suspend fun getArtistsByGenres(
		genres: List<String>?,
		styles: List<String>?,
		page: Int = 1,
		countPerPage: Int = 50
	): HttpResult<DiscogsResponse>

	suspend fun getArtistById(artistId: Int): HttpResult<Artist?>

	suspend fun searchArtistInReleases(artistName: String): HttpResult<Artist?>
	suspend fun getArtistReleases(
		artistId: Int,
		sortType: DiscogsSortType? = null,
		sortOrder: String? = null
	): HttpResult<List<Track>>

	suspend fun searchEntityByType(
		query: String?,
		type: DiscogsSearchType,
		countPerPage: Int = 50
	): HttpResult<DiscogsResponse>

	suspend fun getDiscogsDataFromUrl(url: String): HttpResult<DiscogsResponse>

	fun getArtistPage(
		genreUiState: GenreUiState,
		searchText: String?,
		pageSize: Int,
	): Flow<PagingData<Artist>>
}