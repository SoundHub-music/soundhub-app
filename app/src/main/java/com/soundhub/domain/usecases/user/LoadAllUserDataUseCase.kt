package com.soundhub.domain.usecases.user

import android.util.Log
import com.soundhub.data.api.services.UserService
import com.soundhub.data.model.Artist
import com.soundhub.data.model.User
import com.soundhub.data.repository.MusicRepository
import retrofit2.Response
import javax.inject.Inject

class LoadAllUserDataUseCase @Inject constructor(
	private val musicRepository: MusicRepository,
	private val userService: UserService
) {
	suspend operator fun invoke(user: User) {
		loadUserFriends(user)
		user.friends.forEach { f ->
			loadUserFriends(f)
			loadUserFavoriteArtists(f)
		}
		loadUserFavoriteArtists(user)
	}

	private suspend fun loadUserFriends(user: User) {
		val response: Response<List<User>> = userService.getFriendsByUserId(user.id)

		if (!response.isSuccessful) {
			Log.e(
				"LoadAllUserDataUseCase",
				"message: ${response.message()}\ncode: ${response.code()}"
			)
			return
		}
		val friends = response.body().orEmpty()
		user.friends = friends
	}

	private suspend fun loadUserFavoriteArtists(user: User) {
		val artists = mutableListOf<Artist>()

		user.favoriteArtistsIds.forEach { id ->
			musicRepository.getArtistById(id)
				.onSuccess { response -> response.body?.let { artists.add(it) } }
				.onFailure { e ->
					Log.e(
						"LoadAllUserDataUseCase",
						"loadUserFavoriteArtists: ${e.throwable?.stackTraceToString() ?: e.errorBody.detail}"
					)
				}
		}

		user.favoriteArtists = artists
	}
}