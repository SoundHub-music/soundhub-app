package com.soundhub.di

import com.google.gson.Gson
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.constants.Constants.AUTHORIZED_HTTP_CLIENT_WITH_CACHE
import com.soundhub.utils.constants.Constants.AUTHORIZED_SOUNDHUB_API_RETROFIT
import com.soundhub.utils.constants.Constants.SOUNDHUB_AUTH_SERVICE_RETROFIT
import com.soundhub.utils.constants.Constants.COUNTRIES_API_RETROFIT
import com.soundhub.utils.constants.Constants.LAST_FM_API_RETROFIT
import com.soundhub.utils.constants.Constants.MUSIC_API_RETROFIT
import com.soundhub.utils.constants.Constants.SIMPLE_HTTP_CLIENT
import com.soundhub.utils.constants.Constants.SOUNDHUB_API
import com.soundhub.utils.constants.Constants.UNATHORIZED_HTTP_CLIENT_WITH_CACHE
import com.soundhub.utils.constants.Constants.UNAUTHORIZED_SOUNDHUB_API_RETROFIT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    @Named(AUTHORIZED_SOUNDHUB_API_RETROFIT)
    fun providesSoundHubApiRetrofit(
        @Named(AUTHORIZED_HTTP_CLIENT_WITH_CACHE)
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(SOUNDHUB_API)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    @Named(SOUNDHUB_AUTH_SERVICE_RETROFIT)
    fun providesAuthServiceRetrofit(
        @Named(SIMPLE_HTTP_CLIENT)
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(SOUNDHUB_API)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    @Named(UNAUTHORIZED_SOUNDHUB_API_RETROFIT)
    fun providesUnauthorizedSoundHubApiRetrofit(
        @Named(UNATHORIZED_HTTP_CLIENT_WITH_CACHE)
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SOUNDHUB_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named(COUNTRIES_API_RETROFIT)
    fun providesCountryApiRetrofit(
        @Named(UNATHORIZED_HTTP_CLIENT_WITH_CACHE)
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.COUNTRIES_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named(MUSIC_API_RETROFIT)
    fun providesMusicApiRetrofit(
        @Named(UNATHORIZED_HTTP_CLIENT_WITH_CACHE)
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.DISCOGS_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named(LAST_FM_API_RETROFIT)
    fun providesLastFmRetrofit(
        @Named(UNATHORIZED_HTTP_CLIENT_WITH_CACHE)
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.LAST_FM_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
