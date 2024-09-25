package com.soundhub.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.soundhub.data.model.Post
import com.soundhub.utils.constants.Queries.GET_WALL_POSTS
import com.soundhub.utils.constants.Queries.TRUNCATE_WALL_POSTS

@Dao
interface PostDao {
	@Query(GET_WALL_POSTS)
	suspend fun getWallPosts(): List<Post>

	@Transaction
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun updateWallPosts(posts: List<Post>)

	@Transaction
	@Query(TRUNCATE_WALL_POSTS)
	suspend fun deleteUserPosts()

}