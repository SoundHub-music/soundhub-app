package com.soundhub.data.repository

import com.soundhub.data.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface CountryRepository {
    @GET("all")
    suspend fun getAllCountryNames(): Response<List<Country>>
}