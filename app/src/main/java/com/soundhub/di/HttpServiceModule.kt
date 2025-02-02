package com.soundhub.di

import com.soundhub.data.api.services.AuthService
import com.soundhub.data.api.services.ChatService
import com.soundhub.data.api.services.CountryService
import com.soundhub.data.api.services.DiscogsService
import com.soundhub.data.api.services.FileService
import com.soundhub.data.api.services.GenreService
import com.soundhub.data.api.services.InviteService
import com.soundhub.data.api.services.LastFmService
import com.soundhub.data.api.services.MessageService
import com.soundhub.data.api.services.PostService
import com.soundhub.data.api.services.UserService
import com.soundhub.utils.constants.Constants.AUTHORIZED_SOUNDHUB_API_RETROFIT
import com.soundhub.utils.constants.Constants.COUNTRIES_API_RETROFIT
import com.soundhub.utils.constants.Constants.LAST_FM_API_RETROFIT
import com.soundhub.utils.constants.Constants.MUSIC_API_RETROFIT
import com.soundhub.utils.constants.Constants.SOUNDHUB_AUTH_SERVICE_RETROFIT
import com.soundhub.utils.constants.Constants.UNAUTHORIZED_SOUNDHUB_API_RETROFIT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpServiceModule {
	@Provides
	@Singleton
	fun providesLastFmService(
		@Named(LAST_FM_API_RETROFIT)
		retrofit: Retrofit
	): LastFmService = retrofit.create(LastFmService::class.java)

	@Provides
	@Singleton
	fun providesAuthService(
		@Named(SOUNDHUB_AUTH_SERVICE_RETROFIT)
		retrofit: Retrofit
	): AuthService = retrofit.create(AuthService::class.java)

	@Provides
	@Singleton
	fun providesUserService(
		@Named(AUTHORIZED_SOUNDHUB_API_RETROFIT)
		retrofit: Retrofit
	): UserService = retrofit.create(UserService::class.java)

	@Provides
	@Singleton
	fun providesFileService(
		@Named(AUTHORIZED_SOUNDHUB_API_RETROFIT)
		retrofit: Retrofit
	): FileService = retrofit.create(FileService::class.java)

	@Provides
	@Singleton
	fun providesCountryService(
		@Named(COUNTRIES_API_RETROFIT)
		retrofit: Retrofit
	): CountryService = retrofit.create(CountryService::class.java)

	@Provides
	@Singleton
	fun providesMusicService(
		@Named(MUSIC_API_RETROFIT)
		retrofit: Retrofit
	): DiscogsService = retrofit.create(DiscogsService::class.java)

	@Provides
	@Singleton
	fun providesPostService(
		@Named(AUTHORIZED_SOUNDHUB_API_RETROFIT)
		retrofit: Retrofit
	): PostService = retrofit.create(PostService::class.java)

	@Provides
	@Singleton
	fun providesChatService(
		@Named(AUTHORIZED_SOUNDHUB_API_RETROFIT)
		retrofit: Retrofit
	): ChatService = retrofit.create(ChatService::class.java)

	@Provides
	@Singleton
	fun providesGenreService(
		@Named(UNAUTHORIZED_SOUNDHUB_API_RETROFIT)
		retrofit: Retrofit
	): GenreService = retrofit.create(GenreService::class.java)

	@Provides
	@Singleton
	fun providesInviteService(
		@Named(AUTHORIZED_SOUNDHUB_API_RETROFIT)
		retrofit: Retrofit
	): InviteService = retrofit.create(InviteService::class.java)

	@Provides
	@Singleton
	fun providesMessageService(
		@Named(AUTHORIZED_SOUNDHUB_API_RETROFIT)
		retrofit: Retrofit
	): MessageService = retrofit.create(MessageService::class.java)
}