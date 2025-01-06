package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.soundhub.data.api.responses.discogs.DiscogsEntityResponse
import com.soundhub.data.api.responses.discogs.DiscogsResponse
import com.soundhub.data.api.responses.discogs.artist.DiscogsArtistResponse
import com.soundhub.data.api.responses.internal.ErrorResponse
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.services.GenreService
import com.soundhub.data.api.services.MusicService
import com.soundhub.data.enums.DiscogsSearchType
import com.soundhub.data.enums.DiscogsSortType
import com.soundhub.data.sources.ArtistSource
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Genre
import com.soundhub.domain.model.Track
import com.soundhub.domain.repository.BaseRepository
import com.soundhub.domain.repository.MusicRepository
import com.soundhub.domain.states.GenreUiState
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
	private val musicService: MusicService,
	private val genreService: GenreService,
	private val uiStateDispatcher: UiStateDispatcher,
	context: Context,
	gson: Gson
) : MusicRepository, BaseRepository(gson, context) {
	override suspend fun getAllGenres(countPerPage: Int): HttpResult<List<Genre>> {
		try {
			val response: Response<List<Genre>> = genreService.getAllGenres()
			Log.d("MusicRepository", "getAllGenres[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("MusicRepository", "getAllGenres[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getArtistsByGenres(
		genres: List<String>?,
		styles: List<String>?,
		page: Int,
		countPerPage: Int
	): HttpResult<DiscogsResponse> {
		try {
			val response: Response<DiscogsResponse> = musicService.searchData(
				type = DiscogsSearchType.Release.type,
				genre = genres?.joinToString("|") { it.lowercase() },
				style = styles?.joinToString("|") { it.lowercase() },
				countPerPage = countPerPage,
				page = page
			)
			Log.d("MusicRepository", "loadArtistByGenresToState[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("MusicRepository", "getArtistsByGenres[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}


	override suspend fun searchArtistInReleases(artistName: String): HttpResult<Artist?> {
		try {
			val response: Response<DiscogsResponse> = musicService.searchData(
				query = artistName,
				type = DiscogsSearchType.Artist.type,
				countPerPage = 5
			)
			Log.d("MusicRepository", "searchArtistByName[1]: $response")

			return handleResponse<DiscogsResponse, Artist?>(response) {
				val desiredArtist: Artist? = findArtistOrGetFirst(
					discogsResponseList = response.body()?.results.orEmpty(),
					artistName = artistName
				)

				return@handleResponse HttpResult.Success(body = desiredArtist)
			}
		} catch (e: Exception) {
			Log.e("MusicRepository", "searchArtistByName[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	private fun findArtistOrGetFirst(
		discogsResponseList: List<DiscogsEntityResponse>,
		artistName: String
	): Artist? {
		var desiredArtist: Artist? = null
		discogsResponseList.forEach { artist ->
			if (artist.title.lowercase() == artistName.lowercase()) {
				desiredArtist = Artist(
					id = artist.id,
					name = artist.title,
					cover = artist.coverImage
				)

				return@forEach
			}
		}

		if (desiredArtist == null && discogsResponseList.isNotEmpty()) {
			val firstArtist: DiscogsEntityResponse = discogsResponseList[0]
			desiredArtist = Artist(
				id = firstArtist.id,
				name = firstArtist.title,
				cover = firstArtist.thumb
			)
		}

		return desiredArtist
	}

	override suspend fun getArtistById(artistId: Int): HttpResult<Artist?> {
		try {
			val response: Response<DiscogsArtistResponse> = musicService
				.getArtistById(artistId = artistId)
			Log.d("MusicRepository", "getArtistById[1]: $response")

			return handleResponse<DiscogsArtistResponse, Artist?>(response) {
				val body = response.body()
				var result: Artist? = null
				body?.let {
					result = Artist(
						id = it.id,
						name = it.name,
						cover = it.images[0].uri
					)
				}

				return@handleResponse HttpResult.Success(body = result)
			}

		} catch (e: Exception) {
			Log.e("MusicRepository", "getArtistById[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun searchEntityByType(
		query: String?,
		type: DiscogsSearchType,
		countPerPage: Int
	): HttpResult<DiscogsResponse> {
		try {
			val response: Response<DiscogsResponse> = musicService.searchData(
				query = query,
				type = type.type,
				countPerPage = countPerPage
			)

			Log.d("MusicRepository", "searchArtists[1]: $response")

			return handleResponse<DiscogsResponse>(response)

		} catch (e: Exception) {
			Log.e("MusicRepository", "searchArtists[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getArtistReleases(
		artistId: Int,
		sortType: DiscogsSortType?,
		sortOrder: String?
	): HttpResult<List<Track>> {
		TODO("Not yet implemented")
	}

	override suspend fun getDiscogsDataFromUrl(url: String): HttpResult<DiscogsResponse> {
		try {
			val response: Response<DiscogsResponse> = musicService.getDataFromUrl(url)
			Log.d("MusicRepository", "getDiscogsDataFromUrl[1]: ${response.body()}")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("MusicRepository", "getDiscogsDataFromUrl[3]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				errorBody = ErrorResponse(detail = e.localizedMessage),
				throwable = e
			)
		}
	}

	override fun getArtistPage(
		genreUiState: GenreUiState,
		searchText: String?,
		pageSize: Int
	): Flow<PagingData<Artist>> = Pager(
		config = PagingConfig(pageSize = pageSize),
		pagingSourceFactory = {
			ArtistSource(
				musicRepository = this,
				genreUiState = genreUiState,
				uiStateDispatcher = uiStateDispatcher
			)
		}
	).flow
}