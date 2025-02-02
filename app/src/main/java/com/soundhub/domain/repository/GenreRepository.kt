package com.soundhub.domain.repository

import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.domain.model.Genre

interface GenreRepository {
	suspend fun getAllGenres(countPerPage: Int = 50): HttpResult<List<Genre>>
}