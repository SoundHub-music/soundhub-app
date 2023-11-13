package com.soundhub.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soundhub.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE email = :userEmail")
    suspend fun login(userEmail: String): User?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(user: User)
}