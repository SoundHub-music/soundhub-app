package com.soundhub.di

import com.soundhub.data.local_database.dao.LastFmDao
import com.soundhub.data.local_database.dao.PostDao
import com.soundhub.domain.repository.ChatRepository
import com.soundhub.domain.repository.FileRepository
import com.soundhub.domain.repository.GenreRepository
import com.soundhub.domain.repository.LastFmRepository
import com.soundhub.domain.repository.PostRepository
import com.soundhub.domain.repository.UserRepository
import com.soundhub.domain.usecases.chat.GetAllChatsByUserUseCase
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
import com.soundhub.domain.usecases.file.GetImageUseCase
import com.soundhub.domain.usecases.lastfm.LastFmAuthUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.post.DeletePostByIdUseCase
import com.soundhub.domain.usecases.post.GetPostsByUserUseCase
import com.soundhub.domain.usecases.post.TogglePostLikeAndUpdateListUseCase
import com.soundhub.domain.usecases.user.GetUserByIdUseCase
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
	@Provides
	@Singleton
	fun providesUpdateUserUseCase(
		userRepository: UserRepository
	): UpdateUserUseCase {
		return UpdateUserUseCase(userRepository)
	}

	@Provides
	@Singleton
	fun providesGetImageUseCase(fileRepository: FileRepository): GetImageUseCase {
		return GetImageUseCase(fileRepository)
	}

	@Provides
	@Singleton
	fun providesLoadGenresUseCase(
		genreRepository: GenreRepository,
		uiStateDispatcher: UiStateDispatcher
	): LoadGenresUseCase {
		return LoadGenresUseCase(
			genreRepository = genreRepository,
			uiStateDispatcher = uiStateDispatcher
		)
	}

	@Provides
	@Singleton
	fun providesGetAllChatsByUserUseCase(chatRepository: ChatRepository): GetAllChatsByUserUseCase {
		return GetAllChatsByUserUseCase(chatRepository)
	}

	@Provides
	@Singleton
	fun providesGetOrCreateChatUseCase(chatRepository: ChatRepository): GetOrCreateChatByUserUseCase {
		return GetOrCreateChatByUserUseCase(chatRepository)
	}

	@Provides
	@Singleton
	fun providesGetUserByIdUseCase(userRepository: UserRepository): GetUserByIdUseCase {
		return GetUserByIdUseCase(userRepository)
	}

	@Provides
	@Singleton
	fun providesLoadAllUserDataUseCase(lastFmRepository: LastFmRepository): LoadAllUserDataUseCase {
		return LoadAllUserDataUseCase(lastFmRepository)
	}

	@Provides
	@Singleton
	fun providesGetPostsByUserUseCase(
		postRepository: PostRepository,
		postDao: PostDao
	): GetPostsByUserUseCase {
		return GetPostsByUserUseCase(postRepository, postDao)
	}

	@Provides
	@Singleton
	fun providesDeletePostByIdUseCase(
		postRepository: PostRepository,
	): DeletePostByIdUseCase {
		return DeletePostByIdUseCase(postRepository)
	}

	@Provides
	@Singleton
	fun providesTogglePostLikeAndUpdateListUseCase(
		postRepository: PostRepository
	): TogglePostLikeAndUpdateListUseCase {
		return TogglePostLikeAndUpdateListUseCase(postRepository)
	}

	@Provides
	@Singleton
	fun providesLastFmAuthUseCase(
		lastFmRepository: LastFmRepository,
		lastFmDao: LastFmDao
	): LastFmAuthUseCase {
		return LastFmAuthUseCase(
			lastFmRepository,
			lastFmDao
		)
	}
}