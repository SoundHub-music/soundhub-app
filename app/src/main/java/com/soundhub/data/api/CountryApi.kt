package com.soundhub.data.api

import com.soundhub.data.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface CountryApi {
    @GET("all")
    suspend fun getAllCountryNames(): Response<List<Country>>
}