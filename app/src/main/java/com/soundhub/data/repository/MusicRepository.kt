package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.discogs.DiscogsResponse
import com.soundhub.data.enums.DiscogsSortType
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.data.model.Track

interface MusicRepository {
	suspend fun getAllGenres(countPerPage: Int = 50): HttpResult<List<Genre>>
	suspend fun getArtistsByGenres(
		genres: List<String>,
		styles: List<String> = emptyList(),
		page: Int = 1,
		countPerPage: Int = 100
	): HttpResult<DiscogsResponse>

	suspend fun getArtistById(artistId: Int): HttpResult<Artist?>

	suspend fun searchArtistInReleaseResponse(artistName: String): HttpResult<Artist?>
	suspend fun getArtistReleases(
		artistId: Int,
		sortType: DiscogsSortType? = null,
		sortOrder: String? = null
	): HttpResult<List<Track>>

	suspend fun searchArtists(artistName: String): HttpResult<List<Artist>>
	suspend fun getDiscogsDataFromUrl(url: String): HttpResult<DiscogsResponse>
}