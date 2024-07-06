package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.R
import com.soundhub.data.api.services.GenreService
import com.soundhub.data.api.services.LastFmService
import com.soundhub.data.api.services.MusicService
import com.soundhub.data.api.responses.discogs.artist.DiscogsArtistResponse
import com.soundhub.data.api.responses.discogs.DiscogsEntityResponse
import com.soundhub.data.api.responses.discogs.DiscogsResponse
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.enums.DiscogsSearchType
import com.soundhub.data.enums.DiscogsSortType
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.data.model.Track
import com.soundhub.data.repository.MusicRepository
import com.soundhub.utils.constants.Constants
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val musicService: MusicService,
    private val lastFmService: LastFmService,
    private val genreService: GenreService,
    private val context: Context
): MusicRepository {
    override suspend fun getAllGenres(countPerPage: Int): HttpResult<List<Genre>> {
        try {
            val response: Response<List<Genre>> = genreService.getAllGenres()
            Log.d("MusicRepository", "getAllGenres[1]: $response")

            if (!response.isSuccessful) {
                val errorBody = ErrorResponse(
                        status = response.code(),
                        detail = response.message()
                    )

                Log.e("MusicRepository", "getAllGenres[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("MusicRepository", "getAllGenres[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getArtistsByGenres(
        genres: List<String>,
        styles: List<String>,
        page: Int,
        countPerPage: Int
    ): HttpResult<DiscogsResponse> {
        try {
//            genres.forEach { genre ->
//                val response: Response<ArtistsByTagResponse> = lastFmService.getArtistsByGenre(genre)
//
//                if (!response.isSuccessful) {
//                    Log.e("MusicRepository", "loadArtistByGenresToState[1]: ${response.message()}")
//                }
//                response.body()?.let { artistList ->
//                    artistList.topArtistsBody.artist.forEach { a ->
//                        if (a.name !in artistState.value.artists.map { it.title }) {
//                            val artist = Artist(
//                                title = a.name,
//                                genre = listOf(genre)
//                            )
//                            artistState.update {
//                                it.copy(artists = it.artists + artist)
//                            }
//                        }
//                    }
//
//                }
//
//            }
            val response: Response<DiscogsResponse> = musicService.searchData(
                type = DiscogsSearchType.Release.type,
                genre = genres.joinToString("|") { it.lowercase() },
                style = styles.joinToString("|") { it.lowercase() },
                countPerPage = countPerPage,
                page = page
            )
            Log.d("MusicRepository", "loadArtistByGenresToState[1]: $response")

            if (!response.isSuccessful) {
                val errorBody = ErrorResponse(
                    status = response.code(),
                    detail = response.message()
                )

                Log.e("MusicRepository", "loadArtistByGenresToState[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("MusicRepository", "getArtistsByGenres[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
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

            if (!response.isSuccessful) {
                val errorBody = ErrorResponse(status = response.code(), detail = response.message())

                Log.e("MusicRepository", "searchArtistByName[2]: $errorBody")

                if (response.code() == 429)
                    delay(60000)

                return HttpResult.Error(errorBody = errorBody)
            }

            val desiredArtist: Artist? = findArtistOrGetFirst(
                discogsResponseList = response.body()?.results.orEmpty(),
                artistName = artistName
            )

            return HttpResult.Success(body = desiredArtist)

        }

        catch (e: Exception) {
            Log.e("MusicRepository", "searchArtistByName[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
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
                    title = artist.title,
                    thumb = artist.coverImage
                )

                return@forEach
            }
        }

        if (desiredArtist == null && discogsResponseList.isNotEmpty()) {
            val firstArtist: DiscogsEntityResponse = discogsResponseList[0]
            desiredArtist = Artist(
                id = firstArtist.id,
                title = firstArtist.title,
                thumb = firstArtist.thumb
            )
        }

        return desiredArtist
    }

    override suspend fun getArtistById(artistId: Int): HttpResult<Artist?> {
        try {
            val response: Response<DiscogsArtistResponse> = musicService
                .getArtistById(artistId = artistId)
            Log.d("MusicRepository", "getArtistById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("MusicRepository", "getArtistById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            val body = response.body()
            var result: Artist? = null
            body?.let {
                result = Artist(
                    id = it.id,
                    title = it.name,
                    thumb = it.images[0].uri
                )
            }

            return HttpResult.Success(body = result)
        }
        catch (e: Exception) {
            Log.e("MusicRepository", "getArtistById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun searchArtists(artistName: String): HttpResult<List<Artist>> {
        try {
            val response = musicService.searchData(
                query = artistName,
                type = DiscogsSearchType.Artist.type
            )

            Log.d("MusicRepository", "searchArtists[1]: $response")

            if (!response.isSuccessful) {
                val errorBody = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.e("MusicRepository", "searchArtists[2]: $errorBody")

                return HttpResult.Error(errorBody = errorBody)
            }

            val desiredArtists: List<Artist> = response.body()
                ?.results
                ?.map {
                Artist(
                    id = it.id,
                    title = it.title,
                    genre = it.genre.orEmpty(),
                    style = it.style.orEmpty(),
                    thumb = it.thumb
                )
            }.orEmpty()

            return HttpResult.Success(body = desiredArtists)
        }
        catch (e: Exception) {
            Log.e("MusicRepository", "searchArtists[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
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

            if (!response.isSuccessful) {
                val errorBody = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.e("MusicRepository", "getDiscogsDataFromUrl[2]: $errorBody")

                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("MusicRepository", "getDiscogsDataFromUrl[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }
}