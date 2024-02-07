package com.soundhub.data.repository

import com.soundhub.data.model.Genre
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MusicRepository {
    @GET("genre/all")
    @Headers(
        "Accept: application/json",
        "User-Agent: SoundHub/1.0"
    )
    suspend fun getAllGenres(): Response<GenreRequest>

    @GET("artist")
    @Headers("Accept: application/json")
    suspend fun searchArtistByTags(@Query("tag") tags: List<String>)
}

data class GenreRequest(
    var genres: List<Genre>
)