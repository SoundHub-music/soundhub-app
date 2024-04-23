package com.soundhub.di

import android.app.Application
import com.soundhub.data.datastore.UserCredsStore
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
    WebSocketModule::class
])
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesUserDataStore(app: Application): UserCredsStore = UserCredsStore(app)
}