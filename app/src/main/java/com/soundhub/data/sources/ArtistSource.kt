package com.soundhub.data.sources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.soundhub.data.api.responses.discogs.DiscogsEntityResponse
import com.soundhub.data.api.responses.discogs.DiscogsResponse
import com.soundhub.data.enums.DiscogsSearchType
import com.soundhub.domain.model.Artist
import com.soundhub.domain.repository.MusicRepository
import com.soundhub.domain.states.GenreUiState
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArtistSource @Inject constructor(
	private val musicRepository: MusicRepository,
	private val genreUiState: GenreUiState,
	private val uiStateDispatcher: UiStateDispatcher,
) : PagingSource<String, Artist>() {
	// unique page result cache
	private val loadedArtistIds = mutableSetOf<Int>()
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
			val nextPageUrl: String? = params.key

			if (nextPageUrl == null) fetchArtistsWithoutUrl(Constants.DEFAULT_ARTIST_PAGE_SIZE)
			else fetchArtistsFromNextUrl(nextPageUrl)

		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	private suspend fun fetchArtistsWithoutUrl(countPerPage: Int): LoadResult<String, Artist> {
		return try {
			val uiState = uiStateDispatcher.uiState.firstOrNull()
			val searchText: String? = uiState?.searchBarText

			Log.d("ArtistSource", "fetchArtistsWithoutUrl[search]: $searchText")

			val chosenGenreNames = genreUiState.chosenGenres.mapNotNull { it.name }

			if (searchText?.isNotEmpty() == true) {
				searchArtistJob?.cancelAndJoin()

				var response: DiscogsResponse? = null

				searchArtistJob = scope.launch {
					response = musicRepository.searchEntityByType(
						query = searchText,
						type = DiscogsSearchType.Artist,
						countPerPage = countPerPage
					).getOrThrow()

				}

				searchArtistJob?.join()

				return createPageFromResponse(response)
			}

			val response = musicRepository.getArtistsByGenres(
				genres = chosenGenreNames,
				styles = chosenGenreNames,
				countPerPage = countPerPage
			).getOrThrow()

			createPageFromResponse(response)
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	private suspend fun fetchArtistsFromNextUrl(url: String): LoadResult<String, Artist> {
		return try {
			val response = musicRepository.getDiscogsDataFromUrl(url).getOrThrow()

			createPageFromResponse(response, prevKey = url)
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	private suspend fun createPageFromResponse(
		response: DiscogsResponse?,
		prevKey: String? = null
	): LoadResult<String, Artist> {
		val pagination = response?.pagination
		val data = response?.results.orEmpty()
		val artists = getArtistsFromResponse(data)

		val nextKey = pagination?.urls?.next

		return LoadResult.Page(
			prevKey = if (nextKey == prevKey) null else prevKey,
			nextKey = pagination?.urls?.next,
			data = artists
		)
	}

	private suspend fun getArtistsFromResponse(data: List<DiscogsEntityResponse>): List<Artist> {
		val duplicateEntityRegex = Regex(Constants.DUPLICATE_MUSIC_ENTITY_PATTERN)

		return data.mapNotNull { entity ->
			val artistName = entity.title.substringBefore("-").trim()
			delay(500)

			musicRepository.searchArtistInReleases(artistName)
				.getOrNull()?.let { artistBody ->
					artistBody.genre = entity.genre.orEmpty()
					artistBody.style = entity.style.orEmpty()

					if (!duplicateEntityRegex.matches(artistBody.name.orEmpty())) {
						artistBody
					} else null
				}
		}.filter { it.id !in loadedArtistIds }
			.distinctBy { it.id }
			.also { artists -> loadedArtistIds.addAll(artists.map { it.id }) }
	}
}
