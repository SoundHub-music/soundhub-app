package com.soundhub.domain.usecases.user

import android.util.Log
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.LastFmRepository
import com.soundhub.utils.mappers.ArtistMapper
import javax.inject.Inject

class LoadAllUserDataUseCase @Inject constructor(
	private val lastFmRepository: LastFmRepository,
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
			lastFmRepository.getArtistInfo(mbid = id)
				.onSuccess { response ->
					response.body?.let {
						val mappedArtist = ArtistMapper.impl.lastFmArtistToArtist(it.artist)

						artists.add(mappedArtist)
					}
				}
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