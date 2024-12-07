package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.soundhub.domain.model.CountryName

class CountryNameRoomConverter {
	@TypeConverter
	fun toStringName(countryName: CountryName): String = countryName.common

	@TypeConverter
	fun fromStringName(stringName: String): CountryName = CountryName(stringName)
}