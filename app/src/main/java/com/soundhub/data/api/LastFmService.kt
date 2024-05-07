package com.soundhub.data.api

import com.soundhub.BuildConfig
import com.soundhub.data.api.responses.lastfm.ArtistsByTagResponse
import com.soundhub.data.api.responses.lastfm.LastFmGenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmService {
    @GET("2.0/?method=tag.getTopTags&format=json")
    suspend fun getMusicTags(
        @Query("api_key") apiKey: String = BuildConfig.LAST_FM_API_KEY,
        @Query("num_res") countPerPage: Int = 10
    ): Response<LastFmGenreResponse>

    @GET("2.0/")
    suspend fun getArtistsByTag(
        @Query("api_key") apiKey: String = BuildConfig.LAST_FM_API_KEY,
        @Query("method") method: String = "tag.gettopartists",
        @Query("format") format: String = "json",
        @Query("tag") tag: String,
    ): Response<ArtistsByTagResponse>
}