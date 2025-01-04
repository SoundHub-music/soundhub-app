package com.soundhub.utils.mappers

import com.soundhub.data.api.responses.discogs.DiscogsEntityResponse
import com.soundhub.domain.model.Artist
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface DiscogsMapper {
	@Mapping(target = "name", source = "title")
	@Mapping(target = "cover", source = "thumb")
	fun toArtist(entity: DiscogsEntityResponse): Artist

	companion object {
		val impl: DiscogsMapper = Mappers.getMapper(DiscogsMapper::class.java)
	}
}