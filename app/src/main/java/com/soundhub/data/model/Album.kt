package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

data class Album(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    val title: String = "",

    @SerializedName("releaseDate")
    val releaseDate: LocalDate,

    @SerializedName("genre")
    val genre: Genre? = null
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Album
        return id == other.id &&
                title == other.title &&
                releaseDate == other.releaseDate &&
                genre == other.genre
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + (genre?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Album(id=$id, title='$title', releaseDate=$releaseDate, genre=$genre)"
    }
}
