package com.soundhub.data.api.services

import com.soundhub.data.model.Country
import com.soundhub.utils.constants.ApiEndpoints.Countries.ALL_COUNTRIES
import retrofit2.Response
import retrofit2.http.GET

interface CountryService {
	@GET(ALL_COUNTRIES)
	suspend fun getAllCountryNames(): Response<List<Country>>
}