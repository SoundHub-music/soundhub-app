package com.soundhub.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.enums.ContentTypes
import com.soundhub.utils.enums.MediaFolder
import com.soundhub.utils.enums.UriScheme
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
        private const val FOLDER_NAME_PARAM: String = "?folderName="
        private const val FILE_REQUEST_NAME = "files"

        /**
         * Transforms string access token to bearer token.
         * Returns string without changes if it has bearer token format
         * @param token access token
         */
        fun getBearerToken(token: String?): String =
            if (token?.matches(Regex("Bearer\\s\\S+")) == true)
                token
            else "Bearer ${token?.trim()}"

        /**
         * creates temporary file and multipart form data
         * @param imageUrl url to image file
         * @param context Android context
         */
        suspend fun prepareMediaFormData(imageUrl: String?, context: Context): MultipartBody.Part? {
            val tempFile: File? = createTempMediaFile(
                imageUri = imageUrl,
                context = context
            )

            return createImageFormData(tempFile)
        }

        /**
         * creates http form data with image file
         * @param imageFile media format file
         */
        private fun createImageFormData(imageFile: File?): MultipartBody.Part? {
            if (imageFile == null) return null
            val formData: MultipartBody.Part = MultipartBody.Part.createFormData(
                FILE_REQUEST_NAME, imageFile.name, imageFile
                    .asRequestBody(ContentTypes.IMAGE_ALL.type.toMediaTypeOrNull())
            )
            Log.d("HttpUtils", "getImageFormData[1]: generated formdata: ${formData.body}")
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
                val fileExtension: String = getFileExtension(uri, context)?.let {
                    if (it.isNotEmpty()) ".$it" else ""
                } ?: ""

                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = "temp_image_${UUID.randomUUID()}$fileExtension"
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

        /**
         * returns GlideUrl if URI has http scheme else string image path
         * @param userCreds UserPreferences object with access and refresh tokens
         * @param imageUri image URI object
         * @return GlideUrl or String or null
         */
        fun getGlideUrlOrImagePath(
            userCreds: UserPreferences?,
            imageUri: Uri?,
            mediaFolder: MediaFolder
        ): Any? = imageUri?.let {
            if (imageUri.scheme == UriScheme.HTTP.scheme)
                prepareGlideUrWithAccessToken(userCreds, imageUri.toString(), mediaFolder)
            else imageUri.toString()
        }

        /**
         * prepares image url for glide by adding access token and media folder name
         * @param userCreds creds datastore container
         * @param imageUrl image url (get file endpoint)
         * @param folder enum type with certain folder
         */
        fun prepareGlideUrWithAccessToken(
            userCreds: UserPreferences?,
            imageUrl: String?,
            folder: MediaFolder
        ): GlideUrl? {
            return imageUrl?.let { url ->
                var urlWithParam: String = url
                if (!Regex(Constants.URL_WITH_PARAMS_REGEX).matches(url)) {
                    urlWithParam += FOLDER_NAME_PARAM + folder.folderName
                }

                userCreds?.accessToken?.let { token ->
                    val headers = LazyHeaders.Builder()
                        .addHeader(AUTHORIZATION_HEADER, getBearerToken(token))
                        .build()
                    GlideUrl(urlWithParam, headers)
                }
            }
        }

        // TODO: implement glide url builder for discogs api
        fun prepareGlideUrlDiscogs(imageUrl: String?): GlideUrl? {
            return try {
                if (imageUrl?.isNotEmpty() == true) {
                    val headers = LazyHeaders.Builder()
                    .addHeader(AUTHORIZATION_HEADER, Constants.DISCOGS_AUTHORIZATION)
                        .addHeader("Accept", "*/*")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .addHeader("Accept-Encoding", "gzip, deflate, br")
                        .addHeader("Accept-Language", "ru,en;q=0.9,uk;q=0.8")
                        .addHeader("Cookie", "__cf_bm=elNmsTxHnTIlr..xYvdJvwDb1JVQdwV8CLYZ2dMvRso-1719998487-1.0.1.1-cSDBn.eOwxlGJk0whxna7A56QtrjEWwpHgAtjVB7Fk7eEdWhw8pBY44tcNM0d2Auy6kKjJxiR5TOryyj691fiw")
//                        .addHeader("Cache-Control", "max-age=0")
//                        .addHeader("Referrer", "https://www.discogs.com/")
//                        .addHeader("Origin", "https://i.discogs.com/")
                        .build()

                    val glideUrl = GlideUrl(imageUrl, headers)
                    return glideUrl
                }
                else null
            }
            catch (e: Exception) {
                Log.e("HttpUtils", "prepareGlideUrlDiscogs: ${e.stackTraceToString()}")
                return null
            }
        }

        /**
         * Prepares request builder for glide by adding cache parameters
         * @param context android app context
         * @param imageUrl image url (get file endpoint)
         */
        fun prepareGlideRequestBuilder(
            context: Context,
            imageUrl: String?
        ): RequestBuilder<Drawable> {
            return Glide.with(context)
                .asDrawable()
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        }
    }
}