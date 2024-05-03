package com.soundhub.di

import com.soundhub.data.api.AuthService
import com.soundhub.data.api.ChatService
import com.soundhub.data.api.CountryService
import com.soundhub.data.api.FileService
import com.soundhub.data.api.GenreService
import com.soundhub.data.api.InviteService
import com.soundhub.data.api.LastFmService
import com.soundhub.data.api.MusicService
import com.soundhub.data.api.PostService
import com.soundhub.data.api.UserService
import com.soundhub.utils.constants.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providesLastFmService(
        @Named(Constants.LAST_FM_API_RETROFIT) retrofit: Retrofit
    ): LastFmService = retrofit.create(LastFmService::class.java)

    @Provides
    @Singleton
    fun providesAuthService(
        @Named(Constants.SOUNDHUB_API_RETROFIT) retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesUserService(
        @Named(Constants.SOUNDHUB_API_RETROFIT) retrofit: Retrofit
    ): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providesFileService(
        @Named(Constants.SOUNDHUB_API_RETROFIT) retrofit: Retrofit
    ): FileService = retrofit.create(FileService::class.java)

    @Provides
    @Singleton
    fun providesCountryService(
        @Named(Constants.COUNTRIES_API_RETROFIT) retrofit: Retrofit
    ): CountryService = retrofit.create(CountryService::class.java)

    @Provides
    @Singleton
    fun providesMusicService(
        @Named(Constants.MUSIC_API_RETROFIT) retrofit: Retrofit
    ): MusicService = retrofit.create(MusicService::class.java)

    @Provides
    @Singleton
    fun providesPostService(
        @Named(Constants.SOUNDHUB_API_RETROFIT) retrofit: Retrofit
    ): PostService = retrofit.create(PostService::class.java)

    @Provides
    @Singleton
    fun providesChatService(
        @Named(Constants.SOUNDHUB_API_RETROFIT) retrofit: Retrofit
    ): ChatService = retrofit.create(ChatService::class.java)

    @Provides
    @Singleton
    fun providesGenreService(
        @Named(Constants.SOUNDHUB_API_RETROFIT) retrofit: Retrofit
    ): GenreService = retrofit.create(GenreService::class.java)

    @Provides
    @Singleton
    fun providesInviteService(
        @Named(Constants.SOUNDHUB_API_RETROFIT) retrofit: Retrofit
    ): InviteService = retrofit.create(InviteService::class.java)
}