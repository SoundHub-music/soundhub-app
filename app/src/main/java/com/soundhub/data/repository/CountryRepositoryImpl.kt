package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.services.CountryService
import com.soundhub.domain.model.Country
import com.soundhub.domain.repository.BaseRepository
import com.soundhub.domain.repository.CountryRepository
import retrofit2.Response
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
	private val countryService: CountryService,
	gson: Gson,
	context: Context
) : CountryRepository, BaseRepository(gson, context) {
	override suspend fun getAllCountryNames(): HttpResult<List<Country>> {
		try {
			val response: Response<List<Country>> = countryService.getAllCountryNames()
			Log.d("CountryRepository", "getAllCountryNames[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("CountryRepository", "getAllCountryNames[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}
}