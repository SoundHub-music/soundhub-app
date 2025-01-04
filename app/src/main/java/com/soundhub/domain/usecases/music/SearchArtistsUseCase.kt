package com.soundhub.domain.usecases.music

import android.util.Log
import com.soundhub.data.enums.DiscogsSearchType
import com.soundhub.domain.model.Artist
import com.soundhub.domain.repository.MusicRepository
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.mappers.DiscogsMapper
import javax.inject.Inject

class SearchArtistsUseCase @Inject constructor(
	private val musicRepository: MusicRepository
) {
	suspend operator fun invoke(searchString: String?): Result<List<Artist>> = runCatching {
		var searchResult: List<Artist> = emptyList()
		Log.d("SearchArtistsUseCase", "searchString: $searchString")

		musicRepository.searchEntityByType(searchString, DiscogsSearchType.Artist)
			.onSuccess { response ->
				Log.d("SearchArtistsUseCase", "response: ${response.body}")

				val duplicateEntityRegex = Regex(Constants.DUPLICATE_MUSIC_ENTITY_PATTERN)

				val artists: List<Artist> =
					response.body?.results.orEmpty().map { DiscogsMapper.impl.toArtist(it) }
						.filter { artist ->
							!duplicateEntityRegex.matches(artist.name ?: "")
						}

				searchResult = artists
			}
			.onFailure { error ->
				error.throwable?.let { throw error.throwable }
					?: throw Exception(error.errorBody.detail)
			}

		return@runCatching searchResult
	}
}