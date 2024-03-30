package com.soundhub.data.api

import com.google.gson.annotations.SerializedName
import com.soundhub.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmService {
    @GET("2.0/?method=tag.getTopTags&format=json")
    suspend fun getMusicTags(
        @Query("api_key") apiKey: String = BuildConfig.LAST_FM_API_KEY,
        @Query("num_res") countPerPage: Int = 10
    ): Response<GenreResponse>
}

data class GenreResponse(
    @SerializedName("toptags")
    val topTags: LastFmTopTagsBody,
)

data class LastFmTopTagsBody(
    @SerializedName("@attr")
    val attr: TopTagsAttribute,

    @SerializedName("tag")
    val tag: List<LastFmTag> = emptyList()
)

data class TopTagsAttribute(
    val offset: Int,
    val num_res: Int,
    val total: Int
)

data class LastFmTag(
    val name: String,
    val count: Int,
    val reach: Int
)