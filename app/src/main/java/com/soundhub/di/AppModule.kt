package com.soundhub.di

import android.app.Application
import androidx.room.Room
import com.soundhub.data.UserDatabase
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.AuthRepositoryImpl
import com.soundhub.data.usecases.AuthFormValidationUseCase
import com.soundhub.ui.mainActivity.MainViewModel
import com.soundhub.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesUserDatabase(app: Application): UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            Constants.DB_USERS
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesMainViewModel(): MainViewModel {
        return MainViewModel()
    }


    @Provides
    @Singleton
    fun provideUserRepository(db: UserDatabase): AuthRepository {
        return AuthRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideAuthFormValidationUseCase(): AuthFormValidationUseCase {
        return AuthFormValidationUseCase()
    }
}