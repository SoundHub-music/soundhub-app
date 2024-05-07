package com.soundhub.data.api

import com.soundhub.utils.HttpUtils
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileService {
    @Streaming
    @GET
    suspend fun getFile(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String,
        @Url fileName: String?,
        @Query("folderName")
        folderName: String
    ): Response<ResponseBody>
}