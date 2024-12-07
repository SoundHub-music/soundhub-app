package com.soundhub.domain.usecases.user

import android.util.Log
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.MusicRepository
import javax.inject.Inject

class LoadAllUserDataUseCase @Inject constructor(
	private val musicRepository: MusicRepository,
) {
	suspend operator fun invoke(user: User) {
		user.friends.forEach { f ->
			loadUserFavoriteArtists(f)
		}
		loadUserFavoriteArtists(user)
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