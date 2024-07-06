package com.soundhub.data.api.services

import com.soundhub.data.model.Genre
import com.soundhub.utils.ApiEndpoints.Music.GET_GENRES
import retrofit2.Response
import retrofit2.http.GET

interface GenreService {
    @GET(GET_GENRES)
    suspend fun getAllGenres(): Response<List<Genre>>
}