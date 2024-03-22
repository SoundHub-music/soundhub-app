package com.soundhub.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.soundhub.utils.MediaTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

interface FileRepositoryUtils {
    fun getImageFormData(imageFile: File?, context: Context): MultipartBody.Part? {
        if (imageFile == null) return null
        val formData: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file", imageFile.name, imageFile
                .asRequestBody(MediaTypes.IMAGE_ALL.type.toMediaTypeOrNull())
        )
        Log.d("FileRepositoryUtils", "getImageFormData[1]: generated formdata: $formData")
        return formData
    }

    suspend fun createTempMediaFile(imageUri: String?, context: Context): File? {
        try {
            val uri: Uri = Uri.parse(imageUri)
            val fileExtension: String? = getFileExtension(uri, context)
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)

            Log.d("AuthRepository", "createTempMediaFile[1]: fileExtension = $fileExtension")

            // creating temporary file
            val tempFile: File = withContext(Dispatchers.IO) {
                File.createTempFile("image", ".$fileExtension", context.cacheDir)
            }

            // copying input stream of image uri to temp file
            withContext(Dispatchers.IO) {
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream?.copyTo(outputStream)
                }
            }

            return tempFile
        }
        catch (e: Exception) {
            Log.e("FileRepository", "createTempMediaFile: ${e.stackTraceToString()}")
            return null
        }
    }

    private fun getFileExtension(uri: Uri, context: Context): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        Log.d("AuthRepository", "getFileExtension[1]: fileType = $fileType")
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }
}