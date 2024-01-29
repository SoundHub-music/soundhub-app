package com.soundhub.di

import android.app.Application
import androidx.room.Room
import com.soundhub.BuildConfig
import com.soundhub.data.UserDatabase
import com.soundhub.data.datastore.UserStore
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.AuthRepositoryImpl
import com.soundhub.UiEventDispatcher
import com.soundhub.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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
    fun providesUiEventDispatcher(userStore: UserStore): UiEventDispatcher {
        return UiEventDispatcher(userStore)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(db: UserDatabase): AuthRepository {
        return AuthRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideUserDataStore(app: Application): UserStore {
        return UserStore(app)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_API)
            .client(okHttpClient)
//            .addCallAdapterFactory(GsonConverterFactory.create())
            .build()
    }
}