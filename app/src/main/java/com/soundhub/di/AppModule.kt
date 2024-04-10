package com.soundhub.di

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.api.AuthService
import com.soundhub.data.api.ChatService
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.api.CountryService
import com.soundhub.data.api.FileService
import com.soundhub.data.api.GenreService
import com.soundhub.data.api.LastFmService
import com.soundhub.data.api.MusicService
import com.soundhub.data.api.PostService
import com.soundhub.data.repository.CountryRepository
import com.soundhub.data.repository.MusicRepository
import com.soundhub.data.api.UserService
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.repository.FileRepository
import com.soundhub.data.repository.PostRepository
import com.soundhub.data.repository.implementations.AuthRepositoryImpl
import com.soundhub.data.repository.UserRepository
import com.soundhub.data.repository.implementations.ChatRepositoryImpl
import com.soundhub.data.repository.implementations.CountryRepositoryImpl
import com.soundhub.data.repository.implementations.FileRepositoryImpl
import com.soundhub.data.repository.implementations.MusicRepositoryImpl
import com.soundhub.data.repository.implementations.PostRepositoryImpl
import com.soundhub.data.repository.implementations.UserRepositoryImpl
import com.soundhub.domain.usecases.GetImageUseCase
import com.soundhub.domain.usecases.UpdateUserUseCase
import com.soundhub.utils.Constants
import com.soundhub.utils.converters.LocalDateAdapter
import com.soundhub.utils.converters.LocalDateTimeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Named
import javax.inject.Singleton

// TODO: split di module into ui, domain and data layer modules
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesUiEventDispatcher(): UiStateDispatcher = UiStateDispatcher()

    @Provides
    @Singleton
    fun providesUserDataStore(app: Application): UserCredsStore = UserCredsStore(app)

    @Provides
    @Singleton
    @Named(Constants.COUNTRIES_API_RETROFIT)
    fun providesCountryApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.COUNTRIES_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named(Constants.MUSIC_API_RETROFIT)
    fun providesMusicApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.DISCOGS_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named(Constants.LAST_FM_API_RETROFIT)
    fun providesLastFmRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.LAST_FM_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named(Constants.SOUNDHUB_API_RETROFIT)
    fun providesSoundHubApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
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
    fun providesAuthRepository(
        authService: AuthService,
        @ApplicationContext context: Context
    ): AuthRepository = AuthRepositoryImpl(authService, context)


    @Provides
    @Singleton
    fun providesUserRepository(
        userService: UserService,
        @ApplicationContext context: Context
    ): UserRepository = UserRepositoryImpl(userService, context)

    @Provides
    @Singleton
    fun providesPostRepository(
        postService: PostService,
        @ApplicationContext context: Context
    ): PostRepository = PostRepositoryImpl(postService, context)

    @Provides
    @Singleton
    fun providesCountryRepository(countryService: CountryService): CountryRepository =
        CountryRepositoryImpl(countryService)

    @Provides
    @Singleton
    fun providesChatRepository(
        chatService: ChatService,
        @ApplicationContext context: Context
    ): ChatRepository =
        ChatRepositoryImpl(chatService, context)

    @Provides
    @Singleton
    fun providesFileRepository(
        fileService: FileService,
        @ApplicationContext context: Context
    ): FileRepository = FileRepositoryImpl(fileService, context)

    @Provides
    @Singleton
    fun providesMusicRepository(
        musicService: MusicService,
        genreService: GenreService,
        @ApplicationContext context: Context
    ): MusicRepository = MusicRepositoryImpl(musicService, genreService, context)

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesUpdateUserUseCase(userRepository: UserRepository): UpdateUserUseCase =
        UpdateUserUseCase(userRepository)

    @Provides
    @Singleton
    fun providesGetImageUseCase(fileRepository: FileRepository): GetImageUseCase =
        GetImageUseCase(fileRepository)
}