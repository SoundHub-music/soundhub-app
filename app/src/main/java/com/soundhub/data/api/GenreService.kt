package com.soundhub.data.api

import com.soundhub.data.model.Genre
import retrofit2.Response
import retrofit2.http.GET

interface GenreService {
    @GET("genres")
    suspend fun getAllGenres(): Response<List<Genre>>
}