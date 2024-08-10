package com.soundhub.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soundhub.data.model.Country
import com.soundhub.utils.constants.Queries

@Dao
interface CountryDao {
	@Query(Queries.GET_COUNTRIES)
	suspend fun getCountries(): List<Country>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveCountries(countries: List<Country>)

}