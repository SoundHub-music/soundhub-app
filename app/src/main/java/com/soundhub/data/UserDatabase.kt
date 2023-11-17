package com.soundhub.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soundhub.data.model.User

@Database(
    entities = [User::class],
    version = 2,
    exportSchema = false
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}