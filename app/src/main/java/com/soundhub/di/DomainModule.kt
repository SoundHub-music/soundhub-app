package com.soundhub.di

import com.soundhub.data.repository.FileRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.domain.usecases.GetImageUseCase
import com.soundhub.domain.usecases.UpdateUserUseCase
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
    fun providesUpdateUserUseCase(userRepository: UserRepository): UpdateUserUseCase =
        UpdateUserUseCase(userRepository)

    @Provides
    @Singleton
    fun providesGetImageUseCase(fileRepository: FileRepository): GetImageUseCase =
        GetImageUseCase(fileRepository)
}