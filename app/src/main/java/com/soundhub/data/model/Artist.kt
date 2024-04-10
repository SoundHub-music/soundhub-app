package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Artist(
    val id: Int = 0,
    val name: String,
    val description: String = "",
    var genres: List<String> = emptyList(),
    var styles: List<String> = emptyList(),

    @SerializedName("albums")
    val albums: List<Album> = emptyList(),
    val thumbnailUrl: String? = null
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Artist
        return id == other.id &&
                name == other.name &&
                description == other.description &&
                genres == other.genres &&
                styles == other.styles &&
                albums == other.albums &&
                thumbnailUrl == other.thumbnailUrl
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + genres.hashCode()
        result = 31 * result + styles.hashCode()
        result = 31 * result + albums.hashCode()
        result = 31 * result + (thumbnailUrl?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Artist(id=$id, name='$name', description='$description', genres=$genres, styles=$styles, albums=$albums, thumbnailUrl=$thumbnailUrl)"
    }
}