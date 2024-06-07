package com.soundhub.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [
    UiModule::class,
    RetrofitModule::class,
    HttpServiceModule::class,
    HttpClientModule::class,
    RepositoryModule::class,
    DomainModule::class,
    WebSocketModule::class,
    DataModule::class
])
@InstallIn(SingletonComponent::class)
object AppModule