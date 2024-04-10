package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Genre(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    val name: String? = null,
    val pictureUrl: String? = null
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Genre
        return id == other.id &&
                name == other.name &&
                pictureUrl == other.pictureUrl
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (pictureUrl?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Genre(id=$id, name=$name, pictureUrl=$pictureUrl)"
    }
}
