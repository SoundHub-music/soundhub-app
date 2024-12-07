package com.soundhub.data.repository

import android.content.Context
import android.util.Log
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
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Genre
import com.soundhub.domain.model.Track
import com.soundhub.domain.repository.BaseRepository
import com.soundhub.domain.repository.MusicRepository
import retrofit2.Response
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
	private val musicService: MusicService,
	private val genreService: GenreService,
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
		genres: List<String>,
		styles: List<String>,
		page: Int,
		countPerPage: Int
	): HttpResult<DiscogsResponse> {
		try {
			val response: Response<DiscogsResponse> = musicService.searchData(
				type = DiscogsSearchType.Release.type,
				genre = genres.joinToString("|") { it.lowercase() },
				style = styles.joinToString("|") { it.lowercase() },
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


	override suspend fun searchArtistInReleaseResponse(artistName: String): HttpResult<Artist?> {
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

	override suspend fun searchArtists(artistName: String): HttpResult<List<Artist>> {
		try {
			val response: Response<DiscogsResponse> = musicService.searchData(
				query = artistName,
				type = DiscogsSearchType.Artist.type
			)

			Log.d("MusicRepository", "searchArtists[1]: $response")

			return handleResponse<DiscogsResponse, List<Artist>>(response) {
				val desiredArtists: List<Artist> = response.body()
					?.results
					?.map {
						Artist(
							id = it.id,
							name = it.title,
							genre = it.genre.orEmpty(),
							style = it.style.orEmpty(),
							cover = it.thumb
						)
					}.orEmpty()

				return@handleResponse HttpResult.Success(body = desiredArtists)
			}

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
}