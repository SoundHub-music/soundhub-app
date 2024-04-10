package com.soundhub.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class HttpFileUtils {
    companion object {

        /**
         * creates http form data with image file
         * @param imageFile media format file
         */
        private fun createImageFormData(imageFile: File?): MultipartBody.Part? {
            if (imageFile == null) return null
            val formData: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file", imageFile.name, imageFile
                    .asRequestBody(ContentTypes.IMAGE_ALL.type.toMediaTypeOrNull())
            )
            Log.d("FileRepositoryUtils", "getImageFormData[1]: generated formdata: $formData")
            return formData
        }

        /**
         * creates temporary file in cache directory
         * @param imageUri uri to image file
         * @param context Android context
         */
        private suspend fun createTempMediaFile(imageUri: String?, context: Context): File? {
            try {
                val uri: Uri = Uri.parse(imageUri)
                val fileExtension: String? = getFileExtension(uri, context)

                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = "temp_image${if (!fileExtension.isNullOrEmpty()) ".$fileExtension" else ""}"
                val tempFile = File(context.cacheDir, fileName)


                withContext(Dispatchers.IO) {
                    tempFile.createNewFile()
                    FileOutputStream(tempFile).use { outputStream ->
                        inputStream?.copyTo(outputStream)
                    }
                }

                Log.d("AuthRepository", "createTempMediaFile[1]: fileExtension = $fileExtension")
                return tempFile
            }
            catch (e: Exception) {
                Log.e("FileRepository", "createTempMediaFile: ${e.stackTraceToString()}")
                return null
            }
        }

        /**
         * creates temporary file and multipart form data
         * @param imageUrl url to image file
         * @param context Android context
         */
        suspend fun prepareMediaFormData(imageUrl: String?, context: Context): MultipartBody.Part? {
            var tempFile: File? = null

            if (!imageUrl.isNullOrEmpty())
                tempFile = createTempMediaFile(
                    imageUri = imageUrl,
                    context = context
                )

            return createImageFormData(tempFile)
        }

        /**
         * returns file extension from the specified uri after point
         * @param fileUri uri to file
         * @param context Android context
         */
        private fun getFileExtension(fileUri: Uri, context: Context): String? {
            val fileType: String? = context.contentResolver.getType(fileUri)
            Log.d("HttpUtils", "getFileExtension[1]: fileType = $fileType")
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
        }
    }
}