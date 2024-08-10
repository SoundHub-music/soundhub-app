package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.R
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.services.FileService
import com.soundhub.data.repository.FileRepository
import com.soundhub.utils.constants.Constants
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
	private val context: Context
) : FileRepository {
	private val gson = Gson()

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

			if (!response.isSuccessful) {
				val errorBody: ErrorResponse = gson
					.fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(
						status = response.code(),
						detail = context.getString(R.string.toast_file_loading_error)
					)
				Log.e("FileRepository", "getFile[2]: $errorBody")
				return HttpResult.Error(errorBody = errorBody)
			}

			val file: File = withContext(Dispatchers.IO) {
				File.createTempFile(
					UUID.randomUUID().toString(),
					null,
					context.cacheDir
				)
			}

			Log.d("FileRepository", "getFile[3]: tempFile is $file")

			withContext(Dispatchers.IO) {
				FileOutputStream(file).use {
					response.body()?.byteStream()?.copyTo(it)
				}
			}

			return HttpResult.Success(body = file)
		} catch (e: Exception) {
			Log.e("FileRepository", "getFile[4]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				errorBody = ErrorResponse(detail = e.localizedMessage),
				throwable = e
			)
		}
	}
}