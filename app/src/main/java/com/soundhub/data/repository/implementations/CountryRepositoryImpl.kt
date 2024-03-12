package com.soundhub.data.repository.implementations

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.CountryApi
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Country
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.repository.CountryRepository
import com.soundhub.utils.Constants
import retrofit2.Response
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countryApi: CountryApi
): CountryRepository {
    override suspend fun getAllCountryNames(): HttpResult<List<Country>> {
        try {
            val response: Response<List<Country>> = countryApi.getAllCountryNames()
            Log.d("CountryRepository", "getAllCountryNames[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.d("CountryRepository", "getAllCountryNames[2]: ${errorBody.toString()}")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("CountryRepository", "getAllCountryNames[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(errorBody = ErrorResponse(e.message))
        }
    }
}