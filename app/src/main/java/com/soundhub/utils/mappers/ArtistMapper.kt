package com.soundhub.utils.mappers

import com.soundhub.data.api.responses.lastfm.LastFmArtist
import com.soundhub.data.api.responses.lastfm.LastFmTags
import com.soundhub.domain.model.Artist
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper
interface ArtistMapper {
	@Mapping(source = "name", target = "name")
	@Mapping(source = "mbid", target = "id")
	@Mapping(source = "tags", target = "genre", qualifiedByName = ["mapTags"])
	fun lastFmArtistToArtist(lastFmArtist: LastFmArtist): Artist

	companion object {
		val impl = Mappers.getMapper(ArtistMapper::class.java)

		@JvmStatic
		@Named("mapTags")
		fun mapTags(tags: LastFmTags?): List<String> {
			return tags?.tags?.map { tag -> tag.name }.orEmpty()
		}
	}
}