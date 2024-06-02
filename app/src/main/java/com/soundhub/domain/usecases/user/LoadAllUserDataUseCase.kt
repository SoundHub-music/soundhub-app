package com.soundhub.domain.usecases.user

import android.util.Log
import com.soundhub.data.api.UserService
import com.soundhub.data.model.User
import com.soundhub.data.repository.FileRepository
import com.soundhub.data.repository.MusicRepository
import com.soundhub.utils.enums.MediaFolder
import retrofit2.Response
import javax.inject.Inject

class LoadAllUserDataUseCase @Inject constructor(
    private val musicRepository: MusicRepository,
    private val fileRepository: FileRepository,
    private val userService: UserService
) {
    suspend operator fun invoke(user: User) {
        loadAllUserData(user)
    }

    private suspend fun loadAllUserData(user: User) {
        loadUserFriends(user)
        user.friends.forEach { f -> loadUserFriends(f) }
        loadUserFavoriteArtists(user)
        loadUserAvatar(user)
    }

    private suspend fun loadUserFriends(user: User) {
        val response: Response<List<User>> = userService.getFriendsByUserId(
            userId = user.id
        )

        if (!response.isSuccessful) {
            Log.e("LoadAllUserDataUseCase", "message: ${response.message()}\ncode: ${response.code()}")
            return
        }
        val friends = response.body().orEmpty()
        user.friends = friends
    }

    private suspend fun loadUserFavoriteArtists(user: User) {
        user.favoriteArtists = user.favoriteArtistsIds
            .mapNotNull { artistId -> musicRepository.getArtistById(artistId).getOrNull() }
    }

    private suspend fun loadUserAvatar(user: User) {
        fileRepository.getFile(
            fileNameUrl = user.avatarUrl,
            folderName = MediaFolder.AVATAR.folderName
        )
    }
}