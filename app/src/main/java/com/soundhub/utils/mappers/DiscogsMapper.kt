package com.soundhub.utils.mappers

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface DiscogsMapper {
//	@Mapping(target = "name", source = "title")
//	@Mapping(target = "cover", source = "thumb")
//	@Mapping(target = "discogsId", source = "id")
//	@Mapping(target = "description", ignore = true)
//	@Mapping(target = "albums", ignore = true)
//	fun toArtist(entity: DiscogsEntityResponse): Artist

	companion object {
		val impl: DiscogsMapper = Mappers.getMapper(DiscogsMapper::class.java)
	}
}