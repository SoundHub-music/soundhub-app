package com.soundhub.data.api

import com.soundhub.data.model.Country
import com.soundhub.utils.ApiEndpoints
import retrofit2.Response
import retrofit2.http.GET

interface CountryService {
    @GET(ApiEndpoints.Countries.allCountries)
    suspend fun getAllCountryNames(): Response<List<Country>>
}