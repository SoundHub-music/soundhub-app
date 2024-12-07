package com.soundhub.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.soundhub.utils.converters.room.CountryNameRoomConverter
import com.soundhub.utils.converters.room.CountryTranslationBodyRoomConverter
import com.soundhub.utils.converters.room.CountryTranslationRoomConverter
import java.io.Serializable

@Entity
@TypeConverters(
	CountryNameRoomConverter::class,
	CountryTranslationBodyRoomConverter::class,
	CountryTranslationRoomConverter::class
)
data class Country(
	@PrimaryKey
	@SerializedName("name")
	val name: CountryName,

	@SerializedName("translations")
	val translations: CountryTranslation,

	@SerializedName("cca2")
	val cca2: String
) : Serializable

data class CountryName(
	val common: String
) : Serializable

data class CountryTranslation(
	@SerializedName("rus")
	val rus: CountryTranslationBody
) : Serializable

data class CountryTranslationBody(
	val official: String,
	val common: String
) : Serializable