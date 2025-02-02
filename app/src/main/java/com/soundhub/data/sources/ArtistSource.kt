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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.ceil

class ArtistSource @Inject constructor(
	private val artistRepository: ArtistRepository,
	private val genreUiState: GenreUiState,
	private val uiStateDispatcher: UiStateDispatcher,
) : PagingSource<String, Artist>() {
	// unique page result cache
	private var searchArtistJob: Job? = null
	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	override val keyReuseSupported: Boolean = true

	override fun getRefreshKey(state: PagingState<String, Artist>): String? {
		return state.anchorPosition?.let {
			state.closestPageToPosition(it)?.nextKey ?: state.closestPageToPosition(it)?.prevKey
		}
	}

	override suspend fun load(params: LoadParams<String>): LoadResult<String, Artist> {
		return try {
			val nextPage: String? = params.key

			fetchArtists(Constants.DEFAULT_ARTIST_PAGE_SIZE, nextPage)

		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	private suspend fun fetchArtists(
		countPerPage: Int,
		page: String?
	): LoadResult<String, Artist> {
		return try {
			val uiState: UiState? = uiStateDispatcher.uiState.firstOrNull()
			val searchText: String? = uiState?.searchBarText

			Log.d("ArtistSource", "fetchArtistsWithoutUrl[search]: $searchText")

			val chosenGenreNames = genreUiState.chosenGenres.mapNotNull { it.name }

			if (searchText?.isNotEmpty() == true) {
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

			createPageFromResponse(response)
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	private suspend fun searchArtist(
		query: String,
		countPerPage: Int,
		page: String?
	): LoadResult<String, Artist> {
		searchArtistJob?.cancelAndJoin()
		val mapper = ArtistMapper.impl

		var response: SearchArtistResponseBody? = null

		searchArtistJob = scope.launch {
			response = artistRepository.searchEntityByType(
				query = query,
				countPerPage = countPerPage,
				page = page?.toInt() ?: 1
			).getOrNull()
		}

		searchArtistJob?.join()

		return response?.let {
			val (artistMatches, totalItems, _, perPage, query) = it.results

			val rawArtists: List<LastFmArtist> = artistMatches.artist
			val artists: List<Artist> = rawArtists.map(
				mapper::lastFmArtistToArtist
			)

			val totalPages = BigDecimal(ceil(totalItems.toDouble() / perPage.toDouble())).toString()

			val pagination = LastFmPaginationResponse(
				page = query.startPage,
				perPage = perPage,
				totalPages = totalPages,
			)

			val response = PaginatedArtistsResponse(
				artists = artists,
				pagination = pagination
			)

			return createPageFromResponse(response, query.startPage)
		} ?: throw ArtistNotFoundException()
	}

	private fun createPageFromResponse(
		response: PaginatedArtistsResponse?,
		prevKey: String? = null
	): LoadResult<String, Artist> {
		val pagination: LastFmPaginationResponse? = response?.pagination
		val artists: List<Artist> = response?.artists.orEmpty()

		val totalPages: Int = pagination?.totalPages?.toInt() ?: 0
		val currentPage: Int = pagination?.page?.toInt() ?: 0

		val nextKey = if (currentPage < totalPages) currentPage + 1 else null

		return LoadResult.Page(
			prevKey = if (nextKey.toString() == prevKey) null else prevKey,
			nextKey = nextKey?.toString(),
			data = artists
		)
	}
}
