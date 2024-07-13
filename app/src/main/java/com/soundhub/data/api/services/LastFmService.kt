package com.soundhub.data.api.services

import com.soundhub.data.api.responses.lastfm.ArtistsByTagResponse
import com.soundhub.data.api.responses.lastfm.SearchArtistResponseBody
import com.soundhub.utils.constants.ApiEndpoints.LastFm.GET_TOP_ARTISTS_BY_TAG_ENDPOINT
import com.soundhub.utils.constants.ApiEndpoints.LastFm.SEARCH_ARTISTS_ENDPOINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmService {
    @GET(GET_TOP_ARTISTS_BY_TAG_ENDPOINT)
    suspend fun getArtistsByGenre(
        @Query("tag") tag: String,
        @Query("page") page: Int = 1
    ): Response<ArtistsByTagResponse>

    @GET(SEARCH_ARTISTS_ENDPOINT)
    suspend fun searchArtist(
        @Query("artist") artist: String,
        @Query("page") page: Int = 1
    ): Response<SearchArtistResponseBody>
}