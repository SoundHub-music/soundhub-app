package com.soundhub.data.model

data class Country(
    val name: CountryName,
    val translations: CountryTranslation,
    val cca2: String
)

data class CountryName(
    val common: String
)

data class CountryTranslation(
    val rus: CountryTranslationBody
)

data class CountryTranslationBody(
    val official: String,
    val common: String
)