package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Playlist(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    val name: String = "",

    @SerializedName("author")
    val author: User? = null,

    @SerializedName("tracks")
    val tracks: List<Track> = emptyList()
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist
        return id == other.id &&
                name == other.name &&
                author == other.author &&
                tracks == other.tracks
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + tracks.hashCode()
        return result
    }

    override fun toString(): String {
        return "Playlist(id=$id, name='$name', author=$author, tracks=$tracks)"
    }
}
