package com.soundhub.data.local_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.soundhub.domain.model.User
import com.soundhub.utils.constants.Queries
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
	@Query(Queries.GET_USER)
	suspend fun getCurrentUser(): User?

	@Query(Queries.GET_USER)
	fun observeCurrentUser(): Flow<User?>

	@Transaction
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveUser(user: User)

	suspend fun saveOrReplaceUser(user: User) {
		val currentUser: User? = getCurrentUser()
		if (currentUser != null && currentUser.id != user.id)
			deleteUser(currentUser)

		saveUser(user)
	}

	@Transaction
	@Delete
	suspend fun deleteUser(user: User)

	@Query(Queries.TRUNCATE_USER)
	suspend fun truncateUser()
}