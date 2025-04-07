package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
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
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Track
import com.soundhub.domain.repository.ArtistRepository
import com.soundhub.domain.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
	private val discogsService: DiscogsService,
	private val lastFmService: LastFmService,
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

			return HttpResult.Success(paginatedArtistsData).also {
				parallelLoadDiscogsArtistData(paginatedArtistsData)
			}
		} catch (e: Exception) {
			Log.e("ArtistRepository", "getArtistsByGenres[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun parallelLoadDiscogsArtistData(artistResponse: PaginatedArtistsResponse) {
		CoroutineScope(Dispatchers.IO).launch {
			artistResponse.artists.asFlow()
				.flatMapMerge(concurrency = 6) { artist ->
					flow {
						val response = discogsService.searchData(
							query = artist.name,
							type = DiscogsSearchType.Artist.type
						)

						if (response.code() == 429) {
							Log.d(
								"ArtistRepository",
								"parallelLoadDiscogsArtistData: ${response.code()}"
							)
							// stop making request for 2 minutes
							delay(120 * 1000L)
						}

						response.body()?.let {
							emit(artist to it)
						}
					}
				}
				.collect { (artist, response) -> updateArtistAvatar(artist, response) }
		}
	}

	private fun updateArtistAvatar(artist: Artist, response: DiscogsResponse?) {
		val firstRecord = response?.results?.firstOrNull()

		firstRecord?.let {
			artist.cover = it.thumb
			artist.discogsId = it.id
		}
	}

	private fun mapArtistResponseToArtistList(
		response: ArtistsByTagResponse,
		paginatedArtists: PaginatedArtistsResponse
	) {
		val (artistsResponse, paginationResponse) = response.topArtistsBody
		val (artists) = paginatedArtists

		paginatedArtists.apply {
			if (pagination == null) {
				pagination = paginationResponse
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
}