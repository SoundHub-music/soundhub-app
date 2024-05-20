package com.soundhub.data.api

import com.soundhub.data.api.responses.discogs.artist.DiscogsArtistResponse
import com.soundhub.data.api.responses.discogs.release.DiscogsReleaseResponse
import com.soundhub.data.api.responses.discogs.DiscogsResponse
import com.soundhub.utils.ApiEndpoints
import com.soundhub.utils.ApiEndpoints.Music.GET_ARTISTS
import com.soundhub.utils.ApiEndpoints.Music.ARTIST_ID_DYNAMIC_PARAM
import com.soundhub.utils.ApiEndpoints.Music.GET_ARTIST_RELEASES
import com.soundhub.utils.ApiEndpoints.Music.DATABASE_SEARCH
import com.soundhub.utils.HttpUtils.Companion.AUTHORIZATION_HEADER
import com.soundhub.utils.constants.Constants.DISCOGS_AUTHORIZATION
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MusicService {
    @GET(DATABASE_SEARCH)
    @Headers("$AUTHORIZATION_HEADER: $DISCOGS_AUTHORIZATION")
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
    @Headers("$AUTHORIZATION_HEADER: $DISCOGS_AUTHORIZATION")
    suspend fun getDataFromUrl(@Url url: String): Response<DiscogsResponse>

    @GET(GET_ARTISTS)
    suspend fun getArtistById(
        @Path(ApiEndpoints.Music.ARTIST_ID_DYNAMIC_PARAM) artistId: Int
    ): Response<DiscogsArtistResponse>

    @GET(GET_ARTIST_RELEASES)
    suspend fun getArtistReleases(
        @Path(ARTIST_ID_DYNAMIC_PARAM) artistId: Int,
        @Query("sort") sortType: String? = null,
        @Query("sort_order") sortOrder: String? = null
    ): Response<DiscogsResponse>

    @GET(ApiEndpoints.Music.GET_RELEASE_BY_ID)
    suspend fun getReleaseById(
        @Path(ApiEndpoints.Music.RELEASE_ID_DYNAMIC_PARAM)
        releaseId: Int
    ): Response<DiscogsReleaseResponse>
}

