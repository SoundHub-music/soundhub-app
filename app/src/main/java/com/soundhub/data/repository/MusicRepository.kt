package com.soundhub.data.repository

import com.soundhub.data.api.responses.GenreResponse
import com.soundhub.data.api.responses.HttpResult
import retrofit2.http.Query

interface MusicRepository {
    suspend fun getAllGenres(): HttpResult<GenreResponse>
    suspend fun searchArtistByTags(@Query("tag") tags: List<String>)
}