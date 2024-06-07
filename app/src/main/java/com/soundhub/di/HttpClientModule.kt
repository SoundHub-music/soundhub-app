package com.soundhub.di

import android.content.Context
import com.soundhub.data.api.AuthService
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants.AUTHORIZED_HTTP_CLIENT_WITH_CACHE
import com.soundhub.utils.constants.Constants.SIMPLE_HTTP_CLIENT
import com.soundhub.utils.constants.Constants.UNATHORIZED_HTTP_CLIENT_WITH_CACHE
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
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {
    @Provides
    @Singleton
    fun providesAuthInterceptor(userCredsStore: UserCredsStore): AuthInterceptor =
        AuthInterceptor(userCredsStore)

    @Provides
    @Singleton
    fun providesHttpAuthenticator(
        userCredsStore: UserCredsStore,
        uiStateDispatcher: UiStateDispatcher,
        authService: AuthService
    ): HttpAuthenticator = HttpAuthenticator(
        userCredsStore,
        uiStateDispatcher,
        authService
    )


    @Provides
    @Singleton
    @Named(AUTHORIZED_HTTP_CLIENT_WITH_CACHE)
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
    @Named(UNATHORIZED_HTTP_CLIENT_WITH_CACHE)
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

    @Singleton
    @Provides
    @Named(SIMPLE_HTTP_CLIENT)
    fun providesSimpleOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}