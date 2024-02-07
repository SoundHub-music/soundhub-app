package com.soundhub.di

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.soundhub.BuildConfig
import com.soundhub.data.RoomUserDatabase
import com.soundhub.data.datastore.UserStore
import com.soundhub.data.repository.AuthRepository
import com.soundhub.UiEventDispatcher
import com.soundhub.data.repository.CountryRepository
import com.soundhub.data.repository.MusicRepository
import com.soundhub.data.repository.implementations.AuthRepositoryImpl
import com.soundhub.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesUserDatabase(app: Application): RoomUserDatabase {
        return Room.databaseBuilder(
            app,
            RoomUserDatabase::class.java,
            Constants.DB_USERS
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesUiEventDispatcher(
        userStore: UserStore,
    ): UiEventDispatcher {
        return UiEventDispatcher(userStore)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(db: RoomUserDatabase): AuthRepository {
        return AuthRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideUserDataStore(app: Application): UserStore {
        return UserStore(app)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("user_api")
    fun providesUserApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("country_api")
    fun providesCountryApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://restcountries.com/v3.1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("musicbrainz_api")
    fun providesMusicBrainzRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MUSICBRAINZ_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesCountryRepository(@Named("country_api") retrofit: Retrofit): CountryRepository {
        return retrofit.create(CountryRepository::class.java)
    }

    @Provides
    @Singleton
    fun providesMusicRepository(@Named("musicbrainz_api") retrofit: Retrofit): MusicRepository {
        return retrofit.create(MusicRepository::class.java)
    }

}