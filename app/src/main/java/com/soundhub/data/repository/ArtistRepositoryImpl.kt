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
import com.soundhub.data.api.responses.lastfm.ArtistsByTagResponse
import com.soundhub.data.api.responses.lastfm.PaginatedArtistsResponse
import com.soundhub.data.api.responses.lastfm.SearchArtistResponseBody
import com.soundhub.data.api.services.DiscogsService
import com.soundhub.data.api.services.LastFmService
import com.soundhub.data.enums.DiscogsSearchType
import com.soundhub.data.enums.DiscogsSortType
import com.soundhub.data.sources.ArtistSource
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Track
import com.soundhub.domain.repository.ArtistRepository
import com.soundhub.domain.repository.Repository
import com.soundhub.domain.states.GenreUiState
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
	private val discogsService: DiscogsService,
	private val lastFmService: LastFmService,
	private val uiStateDispatcher: UiStateDispatcher,
	context: Context,
	gson: Gson
) : Repository(gson, context), ArtistRepository {
	override suspend fun getArtistsByGenres(
		genres: List<String>,
		page: Int,
		countPerPage: Int
	): HttpResult<PaginatedArtistsResponse> {
		try {
			val paginatedArtistsData = PaginatedArtistsResponse()

			withContext(Dispatchers.IO) {
				genres.forEach { genre ->
					val response: Response<ArtistsByTagResponse> =
						lastFmService.getArtistsByGenre(
							tag = genre,
							page = page,
						)

					if (!response.isSuccessful) {
						return@forEach
					}

					val body: ArtistsByTagResponse? = response.body()

					body?.let { mapArtistResponseToArtistList(it, paginatedArtistsData) }
				}
			}

			parallelLoadDiscogsArtistData(paginatedArtistsData)

			return HttpResult.Success(paginatedArtistsData)
		} catch (e: Exception) {
			Log.e("ArtistRepository", "getArtistsByGenres[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	private suspend fun parallelLoadDiscogsArtistData(artistResponse: PaginatedArtistsResponse) {
		coroutineScope {
			val deferredTasks = artistResponse.artists.map { artist ->
				async(Dispatchers.IO) {
					val response = discogsService.searchData(
						query = artist.name,
						type = DiscogsSearchType.Artist.type
					).body()

					response?.results?.firstOrNull()?.let { firstRecord ->
						Pair(artist, firstRecord)
					}
				}
			}

			deferredTasks.awaitAll().forEach { pair ->
				val artist = pair?.first
				val record = pair?.second

				artist?.cover = record?.thumb
				artist?.discogsId = record?.id
			}
		}
	}

	private fun mapArtistResponseToArtistList(
		response: ArtistsByTagResponse,
		paginatedArtists: PaginatedArtistsResponse
	) {
		val (artistsResponse, paginationResponse) = response.topArtistsBody
		val (artists, pagination) = paginatedArtists

		paginatedArtists.apply {
			if (pagination == null) {
				this.pagination = paginationResponse
			}
		}

		val transformedArtists: List<Artist> = artistsResponse.mapIndexed { index, artist ->
			val id: Result<UUID> = runCatching {
				UUID.fromString(artist.mbid)
			}

			Artist(
				id = id.getOrElse { UUID.randomUUID() },
				name = artist.name,
			)
		}

		paginatedArtists.artists = (artists + transformedArtists).distinctBy { it.name }
	}


	override suspend fun searchArtistInReleases(artistName: String): HttpResult<Artist?> {
		try {
			val response: Response<DiscogsResponse> = discogsService.searchData(
				query = artistName,
				type = DiscogsSearchType.Artist.type,
				countPerPage = 5
			)
			Log.d("ArtistRepository", "searchArtistByName[1]: $response")

			return handleResponse<DiscogsResponse, Artist?>(response) {
				val desiredArtist: Artist? = findArtistOrGetFirst(
					discogsResponseList = response.body()?.results.orEmpty(),
					artistName = artistName
				)

				return@handleResponse HttpResult.Success(body = desiredArtist)
			}
		} catch (e: Exception) {
			Log.e("ArtistRepository", "searchArtistByName[3]: ${e.stackTraceToString()}")
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
					discogsId = artist.id,
					name = artist.title,
					cover = artist.coverImage
				)

				return@forEach
			}
		}

		if (desiredArtist == null && discogsResponseList.isNotEmpty()) {
			val firstArtist: DiscogsEntityResponse = discogsResponseList[0]
			desiredArtist = Artist(
				discogsId = firstArtist.id,
				name = firstArtist.title,
				cover = firstArtist.thumb
			)
		}

		return desiredArtist
	}

	override suspend fun getArtistById(artistId: Int): HttpResult<Artist?> {
		try {
			val response: Response<DiscogsArtistResponse> = discogsService
				.getArtistById(artistId = artistId)
			Log.d("ArtistRepository", "getArtistById[1]: $response")

			return handleResponse<DiscogsArtistResponse, Artist?>(response) {
				val body = response.body()
				var result: Artist? = null
				body?.let {
					result = Artist(
						discogsId = it.id,
						name = it.name,
						cover = it.images[0].uri
					)
				}

				return@handleResponse HttpResult.Success(body = result)
			}

		} catch (e: Exception) {
			Log.e("ArtistRepository", "getArtistById[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun searchEntityByType(
		query: String?,
		countPerPage: Int,
		page: Int
	): HttpResult<SearchArtistResponseBody> {
		try {
			val response: Response<SearchArtistResponseBody> = lastFmService.searchArtist(
				artist = query,
				page = page,
				limit = countPerPage,
			)

			Log.d("ArtistRepository", "searchEntityByType[1]: $response")

			return handleResponse<SearchArtistResponseBody>(response)

		} catch (e: Exception) {
			Log.e("ArtistRepository", "searchEntityByType[2]: ${e.stackTraceToString()}")
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
			val response: Response<DiscogsResponse> =
				discogsService.getDataFromUrl(url)
			Log.d("ArtistRepository", "getDiscogsDataFromUrl[1]: ${response.body()}")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("ArtistRepository", "getDiscogsDataFromUrl[3]: ${e.stackTraceToString()}")
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
				artistRepository = this,
				genreUiState = genreUiState,
				uiStateDispatcher = uiStateDispatcher
			)
		}
	).flow
}