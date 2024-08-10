package com.soundhub.domain.usecases.music

import com.soundhub.data.model.Artist
import com.soundhub.data.repository.MusicRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class SearchArtistsUseCase @Inject constructor(
	private val musicRepository: MusicRepository
) {
	suspend operator fun invoke(searchString: String): Result<List<Artist>> = runCatching {
		var searchResult: List<Artist> = emptyList()
		delay(500)

		if (searchString.isNotEmpty()) {
			musicRepository.searchArtists(searchString)
				.onSuccess { response ->
					val duplicateEntityRegex = Regex("\\p{L}+(?:\\s+\\p{L}+)*\\s*\\(\\d+\\)")
					val artists: List<Artist> = response.body.orEmpty().filter { artist ->
						!duplicateEntityRegex.matches(artist.name ?: "")
					}

					searchResult = artists
				}
				.onFailure { error ->
					error.throwable?.let { throw error.throwable }
						?: throw Exception(error.errorBody.detail)
				}
		}

		return@runCatching searchResult
	}
}