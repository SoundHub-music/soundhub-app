package com.soundhub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soundhub.data.dao.CountryDao
import com.soundhub.data.dao.UserDao
import com.soundhub.data.model.Album
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Country
import com.soundhub.data.model.Genre
import com.soundhub.data.model.User

@Database(entities = [
    User::class,
    Genre::class,
    Artist::class,
    Album::class,
    Country::class
], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun countryDao(): CountryDao
}