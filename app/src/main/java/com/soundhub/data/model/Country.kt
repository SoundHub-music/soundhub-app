package com.soundhub.data.model

data class Country(
    val name: CountryName,
    val cca2: String
)

data class CountryName(
    val common: String
)