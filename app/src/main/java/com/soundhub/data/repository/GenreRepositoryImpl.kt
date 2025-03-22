package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.services.GenreService
import com.soundhub.domain.model.Genre
import com.soundhub.domain.repository.GenreRepository
import com.soundhub.domain.repository.Repository
import retrofit2.Response
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(
	private val genreService: GenreService,
	context: Context,
	gson: Gson
) : Repository(gson, context), GenreRepository {
	override suspend fun getAllGenres(countPerPage: Int): HttpResult<List<Genre>> {
		try {
			val response: Response<List<Genre>> = genreService.getAllGenres()
			Log.d("MusicRepository", "getAllGenres[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("MusicRepository", "getAllGenres[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}
}