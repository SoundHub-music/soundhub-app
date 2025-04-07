package com.soundhub.domain.repository

import com.soundhub.data.api.responses.discogs.DiscogsResponse
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.lastfm.PaginatedArtistsResponse
import com.soundhub.data.api.responses.lastfm.SearchArtistResponseBody
import com.soundhub.data.enums.DiscogsSortType
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Track

interface ArtistRepository {
	suspend fun getArtistsByGenres(
		genres: List<String>,
		page: Int = 1,
		countPerPage: Int = 50
	): HttpResult<PaginatedArtistsResponse>

	suspend fun getArtistById(artistId: Int): HttpResult<Artist?>

	suspend fun getArtistReleases(
		artistId: Int,
		sortType: DiscogsSortType? = null,
		sortOrder: String? = null
	): HttpResult<List<Track>>

	suspend fun searchEntityByType(
		query: String?,
		countPerPage: Int = 50,
		page: Int = 1
	): HttpResult<SearchArtistResponseBody>

	suspend fun getDiscogsDataFromUrl(url: String): HttpResult<DiscogsResponse>
}