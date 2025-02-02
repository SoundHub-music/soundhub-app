package com.soundhub.di

import android.content.Context
import com.google.gson.Gson
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
import com.soundhub.data.repository.ArtistRepositoryImpl
import com.soundhub.data.repository.AuthRepositoryImpl
import com.soundhub.data.repository.ChatRepositoryImpl
import com.soundhub.data.repository.CountryRepositoryImpl
import com.soundhub.data.repository.FileRepositoryImpl
import com.soundhub.data.repository.GenreRepositoryImpl
import com.soundhub.data.repository.InviteRepositoryImpl
import com.soundhub.data.repository.LastFmRepositoryImpl
import com.soundhub.data.repository.MessageRepositoryImpl
import com.soundhub.data.repository.PostRepositoryImpl
import com.soundhub.data.repository.UserRepositoryImpl
import com.soundhub.domain.repository.ArtistRepository
import com.soundhub.domain.repository.AuthRepository
import com.soundhub.domain.repository.ChatRepository
import com.soundhub.domain.repository.CountryRepository
import com.soundhub.domain.repository.FileRepository
import com.soundhub.domain.repository.GenreRepository
import com.soundhub.domain.repository.InviteRepository
import com.soundhub.domain.repository.LastFmRepository
import com.soundhub.domain.repository.MessageRepository
import com.soundhub.domain.repository.PostRepository
import com.soundhub.domain.repository.UserRepository
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import com.soundhub.presentation.viewmodels.UiStateDispatcher
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
		discogsService: DiscogsService,
		uiStateDispatcher: UiStateDispatcher,
		lastFmService: LastFmService,
		gson: Gson
	): ArtistRepository = ArtistRepositoryImpl(
		uiStateDispatcher = uiStateDispatcher,
		lastFmService = lastFmService,
		discogsService = discogsService,
		context = context,
		gson = gson,
	)

	@Provides
	@Singleton
	fun providesGenreRepository(
		genreService: GenreService,
		gson: Gson,
		@ApplicationContext context: Context
	): GenreRepository = GenreRepositoryImpl(
		genreService = genreService,
		gson = gson,
		context = context
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