package com.soundhub.data.sources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.soundhub.data.api.responses.lastfm.LastFmArtist
import com.soundhub.data.api.responses.lastfm.LastFmPaginationResponse
import com.soundhub.data.api.responses.lastfm.PaginatedArtistsResponse
import com.soundhub.data.api.responses.lastfm.SearchArtistResponseBody
import com.soundhub.data.exceptions.ArtistNotFoundException
import com.soundhub.domain.model.Artist
import com.soundhub.domain.repository.ArtistRepository
import com.soundhub.domain.states.GenreUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.mappers.ArtistMapper
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ArtistSource @Inject constructor(
	private val artistRepository: ArtistRepository,
	private val genreUiState: GenreUiState,
	private val uiStateDispatcher: UiStateDispatcher,
) : PagingSource<String, Artist>() {
	override val keyReuseSupported: Boolean = true

	override fun getRefreshKey(state: PagingState<String, Artist>): String? {
		Log.d("ArtistSource[getRefreshKey]", "${state.anchorPosition}")
		return state.anchorPosition?.let {
			state.closestPageToPosition(it)?.nextKey ?: state.closestPageToPosition(it)?.prevKey
		}
	}

	override suspend fun load(params: LoadParams<String>): LoadResult<String, Artist> {
		try {
			Log.d("ArtistSource[params]", "$params")
			val nextPage: String? = params.key
			val response = fetchArtists(Constants.DEFAULT_ARTIST_PAGE_SIZE, nextPage)

			return response
		} catch (e: Exception) {
			return LoadResult.Error(e)
		}
	}

	private suspend fun fetchArtists(
		countPerPage: Int,
		page: String?
	): LoadResult<String, Artist> {
		try {
			val uiState: UiState? = uiStateDispatcher.uiState.firstOrNull()
			val searchText: String = uiState?.searchBarText.orEmpty()

			Log.d("ArtistSource", "fetchArtists[countPerPage]: $countPerPage")
			Log.d("ArtistSource", "fetchArtists[search]: $searchText")

			val chosenGenreNames = genreUiState.chosenGenres.mapNotNull { it.name }

			if (searchText.isNotEmpty()) {
				return searchArtist(
					query = searchText,
					countPerPage = countPerPage,
					page = page
				)
			}

			val response = artistRepository.getArtistsByGenres(
				genres = chosenGenreNames,
				countPerPage = countPerPage,
				page = page?.toInt() ?: 1
			).getOrThrow()

			return createPageFromResponse(response)
		} catch (e: Exception) {
			return LoadResult.Error(e)
		}
	}

	private suspend fun searchArtist(
		query: String,
		countPerPage: Int,
		page: String?
	): LoadResult<String, Artist> {
		val mapper = ArtistMapper.impl
		var response: SearchArtistResponseBody? = artistRepository.searchEntityByType(
			query = query,
			countPerPage = countPerPage,
			page = page?.toInt() ?: 1
		).onFailure {
			Log.d("ArtistSource", "searchArtist[1]: $it")
		}.onSuccessGet { it.body }

		return response?.let {
			val (artistMatches, totalItems, _, perPage, query) = it.results

			val rawArtists: List<LastFmArtist> =
				artistMatches.artist.filter { it.mbid.isNotEmpty() }
			val artists: List<Artist> = rawArtists.map(mapper::lastFmArtistToArtist)

			val totalPages = runCatching {
				val totalValue = (totalItems.toDouble() / perPage.toDouble())

				totalValue.toInt().toString()
			}.getOrElse { "1" }

			val pagination = LastFmPaginationResponse(
				page = query.startPage,
				perPage = perPage,
				totalPages = totalPages,
			)

			val response = PaginatedArtistsResponse(
				artists = artists,
				pagination = pagination
			)

			return createPageFromResponse(response)
		} ?: throw ArtistNotFoundException()
	}

	private fun createPageFromResponse(
		response: PaginatedArtistsResponse?,
	): LoadResult<String, Artist> {
		val pagination: LastFmPaginationResponse? = response?.pagination
		val artists: List<Artist> = response?.artists
			.orEmpty()
			.distinctBy { it.id }

		val totalPages: Int = runCatching {
			pagination?.totalPages?.toInt() ?: 1
		}.getOrElse { 1 }

		val currentPage: Int = runCatching {
			pagination?.page?.toInt() ?: 1
		}.getOrElse { 1 }

		val prevKey = if (currentPage > 1) (currentPage - 1).toString() else null
		val nextKey = if (currentPage < totalPages) (currentPage + 1).toString() else null

		return LoadResult.Page(
			prevKey = prevKey,
			nextKey = nextKey,
			data = artists
		)
	}
}
