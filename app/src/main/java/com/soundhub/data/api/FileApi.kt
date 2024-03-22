package com.soundhub.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileApi {
    @Streaming
    @GET
    suspend fun getFile(
        @Header("Authorization") accessToken: String,
        @Url fileName: String?,
    ): Response<ResponseBody>
}