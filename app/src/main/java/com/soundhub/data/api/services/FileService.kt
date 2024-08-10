package com.soundhub.data.api.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileService {
	@Streaming
	@GET
	suspend fun getFile(
		@Url fileName: String?,
		@Query("folderName")
		folderName: String? = null
	): Response<ResponseBody>
}