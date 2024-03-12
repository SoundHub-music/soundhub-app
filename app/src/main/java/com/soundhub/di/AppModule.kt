package com.soundhub.di

import android.app.Application
import com.google.gson.GsonBuilder
import com.soundhub.BuildConfig
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.api.AuthApi
import com.soundhub.data.api.ChatApi
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.api.CountryApi
import com.soundhub.data.api.MusicApi
import com.soundhub.data.repository.CountryRepository
import com.soundhub.data.repository.MusicRepository
import com.soundhub.data.api.UserApi
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.repository.implementations.AuthRepositoryImpl
import com.soundhub.data.repository.UserRepository
import com.soundhub.data.repository.implementations.ChatRepositoryImpl
import com.soundhub.data.repository.implementations.CountryRepositoryImpl
import com.soundhub.data.repository.implementations.MusicRepositoryImpl
import com.soundhub.data.repository.implementations.UserRepositoryImpl
import com.soundhub.utils.Constants
import com.soundhub.utils.converters.LocalDateAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesUiEventDispatcher(): UiStateDispatcher = UiStateDispatcher()

    @Provides
    @Singleton
    fun providesUserDataStore(app: Application): UserCredsStore {
        return UserCredsStore(app)
    }

    @Provides
    @Singleton
    @Named(Constants.COUNTRIES_API_LABEL)
    fun providesCountryApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.COUNTRIES_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named(Constants.MUSIC_API_LABEL)
    fun providesMusicBrainzRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MUSICBRAINZ_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named(Constants.SOUNDHUB_API_LABEL)
    fun providesSoundHubApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.SOUNDHUB_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providesAuthApi(@Named(Constants.SOUNDHUB_API_LABEL) retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun providesUserApi(@Named(Constants.SOUNDHUB_API_LABEL) retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun providesCountryApi(@Named(Constants.COUNTRIES_API_LABEL) retrofit: Retrofit): CountryApi {
        return retrofit.create(CountryApi::class.java)
    }

    @Provides
    @Singleton
    fun providesMusicApi(@Named(Constants.MUSIC_API_LABEL) retrofit: Retrofit): MusicApi {
        return retrofit.create(MusicApi::class.java)
    }

    @Provides
    @Singleton
    fun providesChatApi(@Named(Constants.SOUNDHUB_API_LABEL) retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }


    @Provides
    @Singleton
    fun providesUserRepository(userApi: UserApi): UserRepository {
        return UserRepositoryImpl(userApi)
    }

    @Provides
    @Singleton
    fun providesCountryRepository(countryApi: CountryApi): CountryRepository {
        return CountryRepositoryImpl(countryApi)
    }

    @Provides
    @Singleton
    fun providesChatRepository(chatApi: ChatApi): ChatRepository {
        return ChatRepositoryImpl(chatApi)
    }

    @Provides
    @Singleton
    fun providesMusicRepository(musicApi: MusicApi): MusicRepository {
        return MusicRepositoryImpl(musicApi)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}