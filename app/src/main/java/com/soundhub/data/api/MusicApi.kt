package com.soundhub.data.api

import com.soundhub.data.api.responses.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MusicApi {
    @GET("genre/all")
    @Headers(
        "Accept: application/json",
        "User-Agent: SoundHub/1.0"
    )
    suspend fun getAllGenres(): Response<GenreResponse>

    @GET("artist")
    @Headers("Accept: application/json")
    suspend fun searchArtistByTags(@Query("tag") tags: List<String>)
}