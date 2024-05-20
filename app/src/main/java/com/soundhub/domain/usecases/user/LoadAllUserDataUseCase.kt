package com.soundhub.domain.usecases.user

import android.util.Log
import com.soundhub.data.api.UserService
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.data.repository.FileRepository
import com.soundhub.data.repository.MusicRepository
import com.soundhub.utils.HttpUtils
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.Response
import javax.inject.Inject

class LoadAllUserDataUseCase @Inject constructor(
    private val musicRepository: MusicRepository,
    private val fileRepository: FileRepository,
    private val userService: UserService,
    private val userCredsStore: UserCredsStore
) {
    suspend operator fun invoke(user: User) {
        val userCreds: UserPreferences? = userCredsStore.getCreds().firstOrNull()
        loadAllUserData(user, userCreds?.accessToken)
    }

    private suspend fun loadAllUserData(user: User, accessToken: String?) {
        loadUserFriends(user, accessToken)
        user.friends.forEach { f -> loadUserFriends(f, accessToken) }
        loadUserFavoriteArtists(user)
        loadUserAvatar(user, accessToken)
    }

    private suspend fun loadUserFriends(user: User, accessToken: String?) {
        val response: Response<List<User>> = userService.getFriendsByUserId(
            accessToken = HttpUtils.getBearerToken(accessToken),
            userId = user.id
        )

        if (!response.isSuccessful) {
            Log.e("LoadAllUserDataUseCase", "message: ${response.message()}\ncode: ${response.code()}")
        }

        user.friends = response.body().orEmpty()
    }

    private suspend fun loadUserFavoriteArtists(user: User) {
        user.favoriteArtists = user.favoriteArtistsIds
            .mapNotNull { artistId -> musicRepository.getArtistById(artistId).getOrNull() }
    }

    private suspend fun loadUserAvatar(user: User, accessToken: String?) {
        fileRepository.getFile(
            accessToken = accessToken,
            fileNameUrl = user.avatarUrl
        )
    }
}