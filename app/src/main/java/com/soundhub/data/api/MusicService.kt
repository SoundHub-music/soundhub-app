package com.soundhub.data.api

import com.soundhub.data.api.responses.DiscogsReleaseResponse
import com.soundhub.data.api.responses.DiscogsResponse
import com.soundhub.utils.ApiEndpoints
import com.soundhub.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MusicService {
    @GET(ApiEndpoints.Music.databaseSearch)
    @Headers("Authorization: ${Constants.DISCOGS_AUTHORIZATION}")
    suspend fun searchData(
        @Query("q") query: String? = null,
        @Query("type") type: String? = null,
        @Query("title") title: String? = null,
        @Query("release_title") releaseTitle: String? = null,
        @Query("credit") credit: String? = null,
        @Query("artist") artist: String? = null,
        @Query("anv") anv: String? = null,
        @Query("label") label: String? = null,
        @Query("genre") genre: String? = null,
        @Query("style") style: String? = null,
        @Query("country") country: String? = null,
        @Query("year") year: String? = null,
        @Query("format") format: String? = null,
        @Query("catno") catNo: String? = null,
        @Query("barcode") barCode: String? = null,
        @Query("track") track: String? = null,
        @Query("submitter") submitter: String? = null,
        @Query("contributor") contributor: String? = null,
        @Query("per_page") countPerPage: Int = 50,
        @Query("page") page: Int = 1
    ): Response<DiscogsResponse>

    @GET
    @Headers("Authorization: ${Constants.DISCOGS_AUTHORIZATION}")
    suspend fun getDataFromUrl(@Url url: String): Response<DiscogsResponse>

    @GET(ApiEndpoints.Music.artists)
    suspend fun getArtistById(
        @Path(ApiEndpoints.Music.artistIdDynamicParam) artistId: Int
    ): Response<DiscogsResponse>

    @GET(ApiEndpoints.Music.artistReleases)
    suspend fun getArtistReleases(
        @Path(ApiEndpoints.Music.artistIdDynamicParam) artistId: Int,
        @Query("sort") sortType: String? = null,
        @Query("sort_order") sortOrder: String? = null
    ): Response<DiscogsResponse>

    @GET(ApiEndpoints.Music.releases)
    suspend fun getReleaseById(
        @Path(ApiEndpoints.Music.releaseIdDynamicParam)
        releaseId: Int
    ): Response<DiscogsReleaseResponse>
}

