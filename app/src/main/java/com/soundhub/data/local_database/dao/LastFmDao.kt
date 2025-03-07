package com.soundhub.data.local_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.soundhub.domain.model.LastFmUser
import com.soundhub.utils.constants.Queries

@Dao
interface LastFmDao {
	@Query(Queries.GET_LASTFM_USER)
	suspend fun getLastFmSessionUser(): LastFmUser?

	@Transaction
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveLastFmSessionUser(user: LastFmUser)

	@Transaction
	@Delete
	suspend fun deleteLastFmSessionUser(user: LastFmUser)

	@Query(Queries.TRUNCATE_LAST_FM_USER)
	suspend fun truncateLastFmSessionUser()
}