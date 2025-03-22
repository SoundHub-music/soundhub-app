package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.services.FileService
import com.soundhub.domain.repository.FileRepository
import com.soundhub.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
	private val fileService: FileService,
	private val context: Context,
	gson: Gson
) : FileRepository, Repository(gson, context) {
	override suspend fun getFile(
		folderName: String?,
		fileNameUrl: String?,
	): HttpResult<File> {
		try {
			val response: Response<ResponseBody> = fileService.getFile(
				fileName = fileNameUrl,
				folderName = folderName
			)
			Log.d("FileRepository", "getFile[1]: response is $response")

			return handleResponse<ResponseBody, File>(response) {
				val file: File = withContext(Dispatchers.IO) {
					File.createTempFile(
						UUID.randomUUID().toString(),
						null,
						context.cacheDir
					)
				}

				Log.d("FileRepository", "getFile[2]: tempFile is $file")

				withContext(Dispatchers.IO) {
					FileOutputStream(file).use {
						response.body()?.byteStream()?.copyTo(it)
					}
				}

				return@handleResponse HttpResult.Success(body = file)
			}
		} catch (e: Exception) {
			Log.e("FileRepository", "getFile[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}
}