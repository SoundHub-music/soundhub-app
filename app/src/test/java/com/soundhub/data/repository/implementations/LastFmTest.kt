package com.soundhub.data.repository.implementations

import com.soundhub.data.api.services.LastFmService
import com.soundhub.utils.constants.Constants
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LastFmTest {
    private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.LAST_FM_API)
    .client(getHttpClient())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    private val lastFmService: LastFmService = retrofit.create(LastFmService::class.java)

    private fun getHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Test
    fun getArtistsByGenre() = runTest {
//        val artists = lastFmService.getArtistsByTag(tag = "rock")
//        println(artists)

//        assertNotNull(artists)
    }
}