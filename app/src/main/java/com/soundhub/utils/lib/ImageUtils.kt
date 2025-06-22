package com.soundhub.utils.lib

import android.net.Uri
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.enums.MediaFolder
import com.soundhub.utils.enums.UriScheme
import com.soundhub.utils.lib.HttpUtils.Companion.FOLDER_NAME_PARAM

class ImageUtils {
	companion object {
		/**
		 * returns url if URI has http scheme else string file path
		 * @param uri image URI object
		 * @return String or null
		 */
		fun getFileUrlOrLocalPath(
			uri: Uri?,
			mediaFolder: MediaFolder
		): String? = uri?.let {
			if (uri.scheme == UriScheme.HTTP.scheme)
				getImageUrlWithFolderQuery(uri.toString(), mediaFolder.folderName)
			else uri.toString()
		}

		/**
		 * prepares image url by adding media folder name
		 * @param imageUrl image url (get file endpoint)
		 * @param folder enum type with certain folder
		 */
		fun getImageUrlWithFolderQuery(
			imageUrl: String?,
			folder: String
		): String? {
			return imageUrl?.let { url ->
				var urlWithParam: String = url
				val urlRegex = Regex(Constants.URL_WITH_PARAMS_REGEX)

				if (!urlRegex.matches(url)) {
					urlWithParam += FOLDER_NAME_PARAM + folder
				}

				urlWithParam
			}
		}
	}
}