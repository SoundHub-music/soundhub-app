package com.soundhub.data.repository.implementations

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.MusicApi
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.GenreResponse
import com.soundhub.data.repository.MusicRepository
import com.soundhub.utils.Constants
import retrofit2.Response
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val musicApi: MusicApi
): MusicRepository {
    override suspend fun getAllGenres(): HttpResult<GenreResponse> {
        try {
            val genresResponse: Response<GenreResponse> = musicApi.getAllGenres()
            Log.d("MusicRepository", "getAllGenres[1]: $genresResponse")

            if (!genresResponse.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(genresResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.e("MusicRepository", "getAllGenres[2]: ${errorBody.toString()}")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = genresResponse.body())
        }
        catch (e: Exception) {
            Log.e("MusicRepository", "getAllGenres[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(errorBody = ErrorResponse(detail = e.message))
        }
    }

    override suspend fun searchArtistByTags(tags: List<String>) {
        TODO("Not yet implemented")
    }
}