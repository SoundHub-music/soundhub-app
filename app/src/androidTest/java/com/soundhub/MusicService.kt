package com.soundhub

import android.content.Context
import com.soundhub.data.api.LastFmService
import com.soundhub.data.api.MusicService
import com.soundhub.data.repository.MusicRepository
import com.soundhub.data.repository.implementations.MusicRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicService(val context: Context) {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(getHttpLoggingInterceptor())
        .build()

    private val genresApiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.DISCOGS_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val lastFmRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.LAST_FM_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val lastFmService = lastFmRetrofit.create(LastFmService::class.java)
    private val musicService: MusicService = genresApiRetrofit.create(MusicService::class.java)
    private val musicRepository: MusicRepository = MusicRepositoryImpl(
        musicService,
        lastFmService,
        context
    )

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    fun getMusicRepository(): MusicRepository = musicRepository
}