package com.soundhub.di

import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.websocket.WebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
	@Provides
	@Singleton
	fun providesWebSocketClient(userCredsStore: UserCredsStore): WebSocketClient =
		WebSocketClient(userCredsStore)
}