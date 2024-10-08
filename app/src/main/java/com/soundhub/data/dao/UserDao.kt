package com.soundhub.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.soundhub.data.model.User
import com.soundhub.utils.constants.Queries

@Dao
interface UserDao {
	@Query(Queries.GET_USER)
	suspend fun getCurrentUser(): User?

	@Transaction
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveUser(user: User)

	suspend fun saveOrReplaceUser(user: User) {
		val currentUser: User? = getCurrentUser()

		if (currentUser == null)
			saveUser(user)

		if (currentUser?.id != user.id) {
			currentUser?.let {
				deleteUser(currentUser)
				saveUser(user)
			}
		}
	}

	@Transaction
	@Delete
	suspend fun deleteUser(user: User)

	@Query(Queries.TRUNCATE_USER)
	suspend fun truncateUser()
}