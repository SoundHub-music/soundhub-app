package com.soundhub.domain.repository

import com.soundhub.data.api.responses.internal.HttpResult
import java.io.File

interface FileRepository {
	suspend fun getFile(
		folderName: String? = null,
		fileNameUrl: String?,
	): HttpResult<File>
}