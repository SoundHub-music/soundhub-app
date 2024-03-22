package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.BuildConfig
import com.soundhub.data.api.FileApi
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.repository.FileRepository
import com.soundhub.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileApi: FileApi,
    private val context: Context
): FileRepository {
    override suspend fun getFile(
        folderName: String,
        fileNameUrl: String?,
        accessToken: String?
    ): HttpResult<File> {
        try {
            val fileNameWithServerIp = fileNameUrl
                ?.replace("localhost", BuildConfig.SOUNDHUB_API_ADDRESS)
            val response: Response<ResponseBody> = fileApi.getFile(
                accessToken = "Bearer $accessToken",
                fileName = "$fileNameWithServerIp?folderName=$folderName"
            )

            Log.d("FileRepository", "getFile[1]: response is $response")

            if (!response.isSuccessful) {
                val error: ErrorResponse? = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                return HttpResult.Error(
                    errorBody = error
                )
            }

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

            return HttpResult.Success(body = file)
        }
        catch (e: Exception) {
            Log.e("FileRepository", "getFile[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(throwable = e)
        }
    }
}