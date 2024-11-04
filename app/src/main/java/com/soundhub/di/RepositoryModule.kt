package com.soundhub.di

import android.content.Context
import com.google.gson.Gson
import com.soundhub.data.api.services.AuthService
import com.soundhub.data.api.services.ChatService
import com.soundhub.data.api.services.CountryService
import com.soundhub.data.api.services.FileService
import com.soundhub.data.api.services.GenreService
import com.soundhub.data.api.services.InviteService
import com.soundhub.data.api.services.LastFmService
import com.soundhub.data.api.services.MessageService
import com.soundhub.data.api.services.MusicService
import com.soundhub.data.api.services.PostService
import com.soundhub.data.api.services.UserService
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.repository.CountryRepository
import com.soundhub.data.repository.FileRepository
import com.soundhub.data.repository.InviteRepository
import com.soundhub.data.repository.LastFmRepository
import com.soundhub.data.repository.MessageRepository
import com.soundhub.data.repository.MusicRepository
import com.soundhub.data.repository.PostRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.data.repository.implementations.AuthRepositoryImpl
import com.soundhub.data.repository.implementations.ChatRepositoryImpl
import com.soundhub.data.repository.implementations.CountryRepositoryImpl
import com.soundhub.data.repository.implementations.FileRepositoryImpl
import com.soundhub.data.repository.implementations.InviteRepositoryImpl
import com.soundhub.data.repository.implementations.LastFmRepositoryImpl
import com.soundhub.data.repository.implementations.MessageRepositoryImpl
import com.soundhub.data.repository.implementations.MusicRepositoryImpl
import com.soundhub.data.repository.implementations.PostRepositoryImpl
import com.soundhub.data.repository.implementations.UserRepositoryImpl
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
	@Provides
	@Singleton
	fun providesInviteRepository(
		inviteService: InviteService,
		loadAllUserDataUseCase: LoadAllUserDataUseCase,
		gson: Gson,
		@ApplicationContext context: Context
	): InviteRepository = InviteRepositoryImpl(
		inviteService = inviteService,
		loadAllUserDataUseCase = loadAllUserDataUseCase,
		gson = gson,
		context = context
	)

	@Provides
	@Singleton
	fun providesAuthRepository(
		@ApplicationContext
		context: Context,
		authService: AuthService,
		userRepository: UserRepository,
		gson: Gson
	): AuthRepository = AuthRepositoryImpl(
		authService = authService,
		userRepository = userRepository,
		context = context,
		gson = gson
	)

	@Provides
	@Singleton
	fun providesUserRepository(
		@ApplicationContext
		context: Context,
		userService: UserService,
		loadAllUserDataUseCase: LoadAllUserDataUseCase,
		gson: Gson
	): UserRepository = UserRepositoryImpl(
		userService = userService,
		loadAllUserDataUseCase = loadAllUserDataUseCase,
		context = context,
		gson = gson
	)

	@Provides
	@Singleton
	fun providesPostRepository(
		@ApplicationContext
		context: Context,
		postService: PostService,
		loadAllUserDataUseCase: LoadAllUserDataUseCase,
		gson: Gson
	): PostRepository = PostRepositoryImpl(
		postService = postService,
		loadAllUserDataUseCase = loadAllUserDataUseCase,
		context = context,
		gson = gson
	)

	@Provides
	@Singleton
	fun providesCountryRepository(
		countryService: CountryService,
		gson: Gson,
		@ApplicationContext context: Context
	): CountryRepository = CountryRepositoryImpl(
		countryService = countryService,
		gson = gson,
		context = context
	)

	@Provides
	@Singleton
	fun providesChatRepository(
		chatService: ChatService,
		gson: Gson,
		@ApplicationContext context: Context
	): ChatRepository = ChatRepositoryImpl(
		chatService = chatService,
		gson = gson,
		context = context
	)

	@Provides
	@Singleton
	fun providesFileRepository(
		@ApplicationContext
		context: Context,
		gson: Gson,
		fileService: FileService
	): FileRepository = FileRepositoryImpl(
		fileService = fileService,
		context = context,
		gson = gson
	)

	@Provides
	@Singleton
	fun providesMusicRepository(
		@ApplicationContext
		context: Context,
		musicService: MusicService,
		genreService: GenreService,
		gson: Gson
	): MusicRepository = MusicRepositoryImpl(
		musicService = musicService,
		genreService = genreService,
		context = context,
		gson = gson
	)

	@Provides
	@Singleton
	fun providesLastFmRepository(
		lastFmService: LastFmService,
		gson: Gson,
		@ApplicationContext context: Context
	): LastFmRepository = LastFmRepositoryImpl(
		lastFmService = lastFmService,
		gson = gson,
		context = context
	)

	@Provides
	@Singleton
	fun providesMessageRepository(
		messageService: MessageService,
		gson: Gson,
		@ApplicationContext context: Context
	): MessageRepository {
		return MessageRepositoryImpl(
			messageService = messageService,
			gson = gson,
			context = context
		)
	}
}