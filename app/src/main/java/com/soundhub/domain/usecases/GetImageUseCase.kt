package com.soundhub.domain.usecases

import com.soundhub.data.repository.FileRepository
import java.io.File
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(
        accessToken: String?,
        fileName: String?,
        folderName: String
    ): File? {
        var file: File? = null
        fileRepository.getFile(
            accessToken = accessToken,
            fileNameUrl = fileName,
            folderName = folderName
        )
            .onSuccess { file = it.body }
            .onFailure { file = null }
        return file
    }
}