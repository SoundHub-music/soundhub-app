package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Country

interface CountryRepository {
    suspend fun getAllCountryNames(): HttpResult<List<Country>>
}