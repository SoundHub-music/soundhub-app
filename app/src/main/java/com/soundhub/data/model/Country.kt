package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Country(
    @SerializedName("name")
    val name: CountryName,

    @SerializedName("translations")
    val translations: CountryTranslation,

    @SerializedName("cca2")
    val cca2: String
): Serializable

data class CountryName(
    val common: String
): Serializable

data class CountryTranslation(
    @SerializedName("rus")
    val rus: CountryTranslationBody
): Serializable

data class CountryTranslationBody(
    val official: String,
    val common: String
): Serializable