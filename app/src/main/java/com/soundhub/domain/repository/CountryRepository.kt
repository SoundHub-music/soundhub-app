package com.soundhub.domain.repository

import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.model.Country

interface CountryRepository {
	suspend fun getAllCountryNames(): HttpResult<List<Country>>
}