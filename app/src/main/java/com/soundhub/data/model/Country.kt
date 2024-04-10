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
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Country
        return name == other.name &&
                translations == other.translations &&
                cca2 == other.cca2
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + translations.hashCode()
        result = 31 * result + cca2.hashCode()
        return result
    }

    override fun toString(): String {
        return "Country(name=$name, translations=$translations, cca2='$cca2')"
    }
}

data class CountryName(
    val common: String
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CountryName

        return common == other.common
    }

    override fun hashCode(): Int {
        return common.hashCode()
    }

    override fun toString(): String {
        return "CountryName(common='$common')"
    }
}

data class CountryTranslation(
    @SerializedName("rus")
    val rus: CountryTranslationBody
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CountryTranslation

        return rus == other.rus
    }

    override fun hashCode(): Int {
        return rus.hashCode()
    }

    override fun toString(): String {
        return "CountryTranslation(rus=$rus)"
    }
}

data class CountryTranslationBody(
    val official: String,
    val common: String
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CountryTranslationBody

        if (official != other.official) return false
        if (common != other.common) return false

        return true
    }

    override fun hashCode(): Int {
        var result = official.hashCode()
        result = 31 * result + common.hashCode()
        return result
    }

    override fun toString(): String {
        return "CountryTranslationBody(official='$official', common='$common')"
    }
}