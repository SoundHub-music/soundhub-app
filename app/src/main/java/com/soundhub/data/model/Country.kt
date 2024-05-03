package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.soundhub.utils.converters.room.CountryRoomConverter

@Entity
@TypeConverters(CountryRoomConverter::class)
data class Country(
    @PrimaryKey
    @SerializedName("name")
    val name: CountryName,

    @SerializedName("translations")
    val translations: CountryTranslation,

    @SerializedName("cca2")
    val cca2: String
)

data class CountryName(
    val common: String
)

data class CountryTranslation(
    @SerializedName("rus")
    val rus: CountryTranslationBody
)

data class CountryTranslationBody(
    val official: String,
    val common: String
)