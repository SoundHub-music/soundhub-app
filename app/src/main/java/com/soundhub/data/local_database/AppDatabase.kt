package com.soundhub.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soundhub.data.local_database.dao.CountryDao
import com.soundhub.data.local_database.dao.LastFmDao
import com.soundhub.data.local_database.dao.PostDao
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.model.Album
import com.soundhub.domain.model.Artist
import com.soundhub.domain.model.Country
import com.soundhub.domain.model.Genre
import com.soundhub.domain.model.LastFmUser
import com.soundhub.domain.model.Post
import com.soundhub.domain.model.User

@Database(
	entities = [
		User::class,
		Genre::class,
		Artist::class,
		Album::class,
		Country::class,
		LastFmUser::class,
		Post::class
	], version = 8, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun userDao(): UserDao
	abstract fun countryDao(): CountryDao
	abstract fun lastFmDao(): LastFmDao
	abstract fun postDao(): PostDao
}