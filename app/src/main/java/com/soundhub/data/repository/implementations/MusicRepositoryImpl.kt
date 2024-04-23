package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.R
import com.soundhub.data.api.GenreService
import com.soundhub.data.api.MusicService
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
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Response
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val musicService: MusicService,
    private val genreService: GenreService,
    private val context: Context
): MusicRepository {
    override suspend fun getAllGenres(countPerPage: Int): HttpResult<List<Genre>> {
        try {
            val response: Response<List<Genre>> = genreService.getAllGenres()
            Log.d("MusicRepository", "getAllGenres[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?:
                    ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
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

    override suspend fun loadArtistByGenresToState(
        genres: List<String>,
        styles: List<String>,
        page: Int,
        countPerPage: Int,
        artistState: MutableStateFlow<ArtistUiState>
    ): HttpResult<List<Artist>> {
        try {
            val response: Response<DiscogsResponse> = musicService.searchData(
                type = DiscogsSearchType.Release.type,
                genre = genres.joinToString("|") { it.lowercase() },
                style = styles.joinToString("|") { it.lowercase() },
                countPerPage = countPerPage
            )
            Log.d("MusicRepository", "getArtistsByGenres[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)

                Log.e("MusicRepository", "getArtistsByGenres[2]: $errorBody")
                return HttpResult.Error(
                    errorBody = errorBody ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )
                )
            }

            val responseResult: List<DiscogsEntityResponse> = response.body()?.results ?: emptyList()
            loadDataToArtistState(responseResult, artistState)

            return HttpResult.Success(body = null)
        }
        catch (e: Exception) {
            Log.e("MusicRepository", "getArtistsByGenres[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    private suspend fun loadDataToArtistState(
        data: List<DiscogsEntityResponse>,
        state: MutableStateFlow<ArtistUiState>
    ) {
        data.forEach { entity ->
            val artistName = entity.title.split("-")[0].trim()
            searchArtistByName(artistName)
                .onSuccess { artist ->
                    artist.body?.let {
                            artistBody ->
                        artistBody.genres = entity.genre ?: emptyList()
                        artistBody.styles = entity.style ?: emptyList()
                        if (artistBody.name !in state.value.artists.map { it.name })
                            state.update { it.copy(artists = it.artists + artistBody) }
                    }
                }
        }
    }

    override suspend fun getArtistById(artistId: Int): HttpResult<Artist?> {
        try {
            val response: Response<DiscogsArtistResponse> = musicService
                .getArtistById(artistId = artistId)
            Log.d("MusicRepository", "getArtistById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_logout_error)
                    )

                Log.d("ChatRepository", "getAllChatsByCurrentUser[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            val body = response.body()
            var result: Artist? = null
            body?.let {
                result = Artist(
                    id = it.id,
                    name = it.name,
                    thumbnailUrl = it.images[0].uri
                )
            }

            return HttpResult.Success(body = result)
        }
        catch (e: Exception) {
            Log.e("ChatRepository", "getAllChatsByCurrentUser[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    // TODO: find out where to apply it
    private suspend fun searchSeveralArtistsByNames(artists: List<String>): HttpResult<List<Artist>> {
        try {
            val response = musicService.searchData(
                query = artists.joinToString("|"),
                type = DiscogsSearchType.Artist.type
            )

            if (!response.isSuccessful) {
                val errorBody = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                return HttpResult.Error(
                    errorBody = errorBody
                )
            }
            val desiredArtists: List<Artist> = response.body()
                ?.results
                ?.map {
                Artist(
                    id = it.id,
                    name = it.title,
                    genres = it.genre ?: emptyList(),
                    styles = it.style ?: emptyList(),
                    thumbnailUrl = it.thumb
                )
            } ?: emptyList()

            return HttpResult.Success(body = desiredArtists)
        }
        catch (e: Exception) {
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun searchArtistByName(artistName: String): HttpResult<Artist?> {
        try {
            val response: Response<DiscogsResponse> = musicService.searchData(
                query = artistName,
                type = DiscogsSearchType.Artist.type,
                countPerPage = 5
            )
            Log.d("MusicRepository", "searchArtistByName[1]: $response")
            var desiredArtist: Artist? = null

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE) ?:
                    ErrorResponse(status = response.code(), detail = context.getString(R.string.toast_common_error))

                Log.e("MusicRepository", "searchArtistByName[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            val responseResult = response.body()?.results ?: emptyList()
            responseResult.forEach { artist ->
                if (artist.title.lowercase() == artistName.lowercase())
                    desiredArtist = Artist(
                        id = artist.id,
                        name = artist.title,
                        thumbnailUrl = artist.coverImage
                    )
            }

            if (desiredArtist == null && responseResult.isNotEmpty()) {
                val firstArtist: DiscogsEntityResponse = responseResult[0]
                desiredArtist = Artist(
                    id = firstArtist.id,
                    name = firstArtist.title,
                    thumbnailUrl = firstArtist.thumb
                )
            }

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

    override suspend fun getArtistReleases(
        artistId: Int,
        sortType: DiscogsSortType?,
        sortOrder: String?
    ): HttpResult<List<Track>> {
        TODO("Not yet implemented")
    }
}