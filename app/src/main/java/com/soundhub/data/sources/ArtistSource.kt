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
import com.soundhub.utils.constants.Constants
import javax.inject.Inject

class ArtistSource @Inject constructor(
	private val musicRepository: MusicRepository,
	private val genreUiState: GenreUiState,
	private val searchText: String? = null
) : PagingSource<String, Artist>() {

	override fun getRefreshKey(state: PagingState<String, Artist>): String? {
		return state.anchorPosition?.let {
			state.closestPageToPosition(it)?.nextKey ?: state.closestPageToPosition(it)?.prevKey
		}
	}

	override suspend fun load(params: LoadParams<String>): LoadResult<String, Artist> {
		return try {
			Log.d("ArtistSource", "params: ${params.loadSize}")
			val nextPageUrl: String? = params.key

			if (nextPageUrl == null) fetchArtistsWithoutUrl(Constants.DEFAULT_ARTIST_PAGE_SIZE)
			else fetchArtistsFromNextUrl(nextPageUrl)

		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	private suspend fun fetchArtistsWithoutUrl(loadSize: Int): LoadResult<String, Artist> {
		return try {
			val chosenGenreNames = genreUiState.chosenGenres.mapNotNull { it.name }
			var response: DiscogsResponse? = null

			response = (if (searchText?.isNotEmpty() == true) {
				musicRepository.searchEntityByType(
					query = searchText,
					type = DiscogsSearchType.Artist,
					countPerPage = loadSize
				)
			} else {
				musicRepository.getArtistsByGenres(
					genres = chosenGenreNames,
					styles = chosenGenreNames,
					countPerPage = loadSize
				)
			}).getOrThrow()

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

			musicRepository.searchArtistInReleases(artistName)
				.getOrNull()?.let { artistBody ->
					artistBody.genre = entity.genre.orEmpty()
					artistBody.style = entity.style.orEmpty()

					if (!duplicateEntityRegex.matches(artistBody.name.orEmpty())) {
						artistBody
					} else null
				}
		}.distinctBy { it.name }.distinctBy { it.id }
	}
}
