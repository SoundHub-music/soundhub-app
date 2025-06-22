package com.soundhub.utils.lib

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.soundhub.utils.enums.ContentTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class HttpUtils {
	companion object {
		const val AUTHORIZATION_HEADER: String = "Authorization"
		const val FOLDER_NAME_PARAM: String = "?folderName="
		private const val FILE_REQUEST_NAME = "files"

		/**
		 * Transforms string access token to bearer token.
		 * Returns string without changes if it has bearer token format
		 * @param token access token
		 */
		fun getBearerToken(token: String?): String =
			if (token?.matches(Regex("Bearer\\s\\S+")) == true)
				token
			else
				"Bearer ${token?.trim()}"

		/**
		 * creates temporary file and multipart form data
		 * @param imageUrl url to image file
		 * @param context Android context
		 */
		suspend fun prepareMediaFormData(
			imageUrl: String?,
			fileName: String?,
			context: Context
		): MultipartBody.Part? {
			val tempFile: File? = createTempMediaFile(
				imageUri = imageUrl,
				context = context,
				fileName = fileName
			)

			return createImageFormData(tempFile)
		}

		/**
		 * creates http form data with image file
		 * @param imageFile media format file
		 */
		private fun createImageFormData(imageFile: File?): MultipartBody.Part? {
			if (imageFile == null) return null

			val requestBody = imageFile.asRequestBody(
				ContentTypes.IMAGE_ALL.type.toMediaTypeOrNull()
			)

			val formData: MultipartBody.Part = MultipartBody.Part.createFormData(
				FILE_REQUEST_NAME,
				imageFile.name,
				requestBody
			)
			Log.d("HttpUtils", "getImageFormData[1]: generated formdata: ${formData.body}")
			return formData
		}

		/**
		 * creates temporary file in cache directory
		 * @param imageUri uri to image file
		 * @param context Android context
		 */
		private suspend fun createTempMediaFile(
			imageUri: String?,
			fileName: String?,
			context: Context
		): File? {
			try {
				val uri: Uri? = imageUri?.toUri()

				if (uri == null) {
					return null
				}

				val fileExtension: String = getFileExtension(uri, context)?.let {
					if (it.isNotEmpty()) ".$it" else ""
				} ?: ""

				val inputStream = context.contentResolver.openInputStream(uri)
				var fileName = fileName ?: UUID.randomUUID().toString()

				fileName += fileExtension

				val tempFile = File(context.cacheDir, fileName)

				withContext(Dispatchers.IO) {
					tempFile.createNewFile()
					FileOutputStream(tempFile).use { outputStream ->
						inputStream?.copyTo(outputStream)
					}
				}

				Log.d("HttpUtils", "createTempMediaFile[1]: fileExtension = $fileExtension")
				return tempFile
			} catch (e: Exception) {
				Log.e("HttpUtils", "createTempMediaFile[2]: ${e.stackTraceToString()}")
				return null
			}
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