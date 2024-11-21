package com.soundhub.di

import com.soundhub.data.api.websocket.WebSocketClient
import com.soundhub.data.datastore.UserCredsStore
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