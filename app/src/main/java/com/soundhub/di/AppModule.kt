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
    HttpServiceModule::class,
    RepositoryModule::class,
    DomainModule::class,
    WebSocketModule::class,
    DataModule::class
])
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesApplication(app: Application): Application = app
}