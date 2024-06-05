package com.soundhub.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.constants.Constants.AUTHORIZED_HTTP_CLIENT
import com.soundhub.utils.constants.Constants.AUTHORIZED_SOUNDHUB_API_RETROFIT
import com.soundhub.utils.constants.Constants.COUNTRIES_API_RETROFIT
import com.soundhub.utils.constants.Constants.LAST_FM_API_RETROFIT
import com.soundhub.utils.constants.Constants.MUSIC_API_RETROFIT
import com.soundhub.utils.constants.Constants.UNATHORIZED_HTTP_CLIENT
import com.soundhub.utils.constants.Constants.UNAUTHORIZED_SOUNDHUB_API_RETROFIT
import com.soundhub.utils.converters.json.LocalDateAdapter
import com.soundhub.utils.converters.json.LocalDateTimeAdapter
import com.soundhub.utils.request_interceptors.AuthInterceptor
import com.soundhub.utils.request_interceptors.CacheInterceptor
import com.soundhub.utils.request_interceptors.ForceCacheInterceptor
import com.soundhub.utils.request_interceptors.HttpAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesAuthInterceptor(userCredsStore: UserCredsStore): AuthInterceptor =
        AuthInterceptor(userCredsStore)

    @Provides
    @Singleton
    fun providesHttpAuthenticator(
        userCredsStore: UserCredsStore,
        uiStateDispatcher: UiStateDispatcher
    ): HttpAuthenticator = HttpAuthenticator(userCredsStore, uiStateDispatcher)


    @Provides
    @Singleton
    @Named(AUTHORIZED_HTTP_CLIENT)
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        authInterceptor: AuthInterceptor,
        authenticator: HttpAuthenticator
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val cacheSize: Long = 10 * 1024 * 1024 // 10 MB
        val cacheDirectory = File(context.cacheDir, "http-cache")
        val cache = Cache(cacheDirectory, cacheSize)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(ForceCacheInterceptor(context))
            .addNetworkInterceptor(CacheInterceptor())
            .authenticator(authenticator)
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    @Named(UNATHORIZED_HTTP_CLIENT)
    fun providesUnauthorizedOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val cacheSize: Long = 10 * 1024 * 1024 // 10 MB
        val cacheDirectory = File(context.cacheDir, "http-cache")
        val cache = Cache(cacheDirectory, cacheSize)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ForceCacheInterceptor(context))
            .addNetworkInterceptor(CacheInterceptor())
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    @Named(AUTHORIZED_SOUNDHUB_API_RETROFIT)
    fun providesSoundHubApiRetrofit(
        @Named(AUTHORIZED_HTTP_CLIENT)
        okHttpClient: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl(Constants.SOUNDHUB_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named(UNAUTHORIZED_SOUNDHUB_API_RETROFIT)
    fun providesUnauthorizedSoundHubApiRetrofit(
        @Named(UNATHORIZED_HTTP_CLIENT)
        okHttpClient: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl(Constants.SOUNDHUB_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named(COUNTRIES_API_RETROFIT)
    fun providesCountryApiRetrofit(
        @Named(UNATHORIZED_HTTP_CLIENT)
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
        @Named(UNATHORIZED_HTTP_CLIENT)
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
        @Named(UNATHORIZED_HTTP_CLIENT)
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.LAST_FM_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
