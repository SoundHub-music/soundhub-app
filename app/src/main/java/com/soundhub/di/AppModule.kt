package com.soundhub.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [
    UiModule::class,
    NetworkModule::class,
    ServiceModule::class,
    RepositoryModule::class,
    DomainModule::class,
    WebSocketModule::class,
    DataModule::class
])
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesApplication(application: Application): Application = application
}